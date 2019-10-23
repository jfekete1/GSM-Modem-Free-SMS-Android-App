package android.arch.lifecycle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

@RestrictTo({Scope.LIBRARY_GROUP})
class Lifecycling {
    private static Map<Class, Constructor<? extends GenericLifecycleObserver>> sCallbackCache = new HashMap();
    private static Constructor<? extends GenericLifecycleObserver> sREFLECTIVE;

    Lifecycling() {
    }

    static {
        try {
            sREFLECTIVE = ReflectiveGenericLifecycleObserver.class.getDeclaredConstructor(new Class[]{Object.class});
        } catch (NoSuchMethodException unused) {
        }
    }

    @NonNull
    static GenericLifecycleObserver getCallback(Object obj) {
        if (obj instanceof GenericLifecycleObserver) {
            return (GenericLifecycleObserver) obj;
        }
        try {
            Class cls = obj.getClass();
            Constructor constructor = (Constructor) sCallbackCache.get(cls);
            if (constructor != null) {
                return (GenericLifecycleObserver) constructor.newInstance(new Object[]{obj});
            }
            Constructor<? extends GenericLifecycleObserver> generatedAdapterConstructor = getGeneratedAdapterConstructor(cls);
            if (generatedAdapterConstructor == null) {
                generatedAdapterConstructor = sREFLECTIVE;
            } else if (!generatedAdapterConstructor.isAccessible()) {
                generatedAdapterConstructor.setAccessible(true);
            }
            sCallbackCache.put(cls, generatedAdapterConstructor);
            return (GenericLifecycleObserver) generatedAdapterConstructor.newInstance(new Object[]{obj});
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e2) {
            throw new RuntimeException(e2);
        } catch (InvocationTargetException e3) {
            throw new RuntimeException(e3);
        }
    }

    @Nullable
    private static Constructor<? extends GenericLifecycleObserver> getGeneratedAdapterConstructor(Class<?> cls) {
        Package packageR = cls.getPackage();
        String name = packageR != null ? packageR.getName() : "";
        String canonicalName = cls.getCanonicalName();
        if (canonicalName == null) {
            return null;
        }
        if (!name.isEmpty()) {
            canonicalName = canonicalName.substring(name.length() + 1);
        }
        String adapterName = getAdapterName(canonicalName);
        try {
            if (!name.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                sb.append(name);
                sb.append(".");
                sb.append(adapterName);
                adapterName = sb.toString();
            }
            return Class.forName(adapterName).getDeclaredConstructor(new Class[]{cls});
        } catch (ClassNotFoundException unused) {
            Class superclass = cls.getSuperclass();
            if (superclass != null) {
                return getGeneratedAdapterConstructor(superclass);
            }
            return null;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    static String getAdapterName(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(str.replace(".", "_"));
        sb.append("_LifecycleAdapter");
        return sb.toString();
    }
}
