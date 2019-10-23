package android.arch.lifecycle;

import android.arch.lifecycle.Lifecycle.Event;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

class ReflectiveGenericLifecycleObserver implements GenericLifecycleObserver {
    private static final int CALL_TYPE_NO_ARG = 0;
    private static final int CALL_TYPE_PROVIDER = 1;
    private static final int CALL_TYPE_PROVIDER_WITH_EVENT = 2;
    static final Map<Class, CallbackInfo> sInfoCache = new HashMap();
    private final CallbackInfo mInfo = getInfo(this.mWrapped.getClass());
    private final Object mWrapped;

    static class CallbackInfo {
        final Map<Event, List<MethodReference>> mEventToHandlers = new HashMap();
        final Map<MethodReference, Event> mHandlerToEvent;

        CallbackInfo(Map<MethodReference, Event> map) {
            this.mHandlerToEvent = map;
            for (Entry entry : map.entrySet()) {
                Event event = (Event) entry.getValue();
                List list = (List) this.mEventToHandlers.get(event);
                if (list == null) {
                    list = new ArrayList();
                    this.mEventToHandlers.put(event, list);
                }
                list.add(entry.getKey());
            }
        }
    }

    static class MethodReference {
        final int mCallType;
        final Method mMethod;

        MethodReference(int i, Method method) {
            this.mCallType = i;
            this.mMethod = method;
            this.mMethod.setAccessible(true);
        }

        public boolean equals(Object obj) {
            boolean z = true;
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            MethodReference methodReference = (MethodReference) obj;
            if (this.mCallType != methodReference.mCallType || !this.mMethod.getName().equals(methodReference.mMethod.getName())) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            return (this.mCallType * 31) + this.mMethod.getName().hashCode();
        }
    }

    ReflectiveGenericLifecycleObserver(Object obj) {
        this.mWrapped = obj;
    }

    public void onStateChanged(LifecycleOwner lifecycleOwner, Event event) {
        invokeCallbacks(this.mInfo, lifecycleOwner, event);
    }

    private void invokeMethodsForEvent(List<MethodReference> list, LifecycleOwner lifecycleOwner, Event event) {
        if (list != null) {
            for (int size = list.size() - 1; size >= 0; size--) {
                invokeCallback((MethodReference) list.get(size), lifecycleOwner, event);
            }
        }
    }

    private void invokeCallbacks(CallbackInfo callbackInfo, LifecycleOwner lifecycleOwner, Event event) {
        invokeMethodsForEvent((List) callbackInfo.mEventToHandlers.get(event), lifecycleOwner, event);
        invokeMethodsForEvent((List) callbackInfo.mEventToHandlers.get(Event.ON_ANY), lifecycleOwner, event);
    }

    private void invokeCallback(MethodReference methodReference, LifecycleOwner lifecycleOwner, Event event) {
        try {
            switch (methodReference.mCallType) {
                case 0:
                    methodReference.mMethod.invoke(this.mWrapped, new Object[0]);
                    return;
                case 1:
                    methodReference.mMethod.invoke(this.mWrapped, new Object[]{lifecycleOwner});
                    return;
                case 2:
                    methodReference.mMethod.invoke(this.mWrapped, new Object[]{lifecycleOwner, event});
                    return;
                default:
                    return;
            }
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Failed to call observer method", e.getCause());
        } catch (IllegalAccessException e2) {
            throw new RuntimeException(e2);
        }
    }

    private static CallbackInfo getInfo(Class cls) {
        CallbackInfo callbackInfo = (CallbackInfo) sInfoCache.get(cls);
        if (callbackInfo != null) {
            return callbackInfo;
        }
        return createInfo(cls);
    }

    private static void verifyAndPutHandler(Map<MethodReference, Event> map, MethodReference methodReference, Event event, Class cls) {
        Event event2 = (Event) map.get(methodReference);
        if (event2 != null && event != event2) {
            Method method = methodReference.mMethod;
            StringBuilder sb = new StringBuilder();
            sb.append("Method ");
            sb.append(method.getName());
            sb.append(" in ");
            sb.append(cls.getName());
            sb.append(" already declared with different @OnLifecycleEvent value: previous");
            sb.append(" value ");
            sb.append(event2);
            sb.append(", new value ");
            sb.append(event);
            throw new IllegalArgumentException(sb.toString());
        } else if (event2 == null) {
            map.put(methodReference, event);
        }
    }

    private static CallbackInfo createInfo(Class cls) {
        int i;
        Class superclass = cls.getSuperclass();
        HashMap hashMap = new HashMap();
        if (superclass != null) {
            CallbackInfo info = getInfo(superclass);
            if (info != null) {
                hashMap.putAll(info.mHandlerToEvent);
            }
        }
        Method[] declaredMethods = cls.getDeclaredMethods();
        for (Class info2 : cls.getInterfaces()) {
            for (Entry entry : getInfo(info2).mHandlerToEvent.entrySet()) {
                verifyAndPutHandler(hashMap, (MethodReference) entry.getKey(), (Event) entry.getValue(), cls);
            }
        }
        for (Method method : declaredMethods) {
            OnLifecycleEvent onLifecycleEvent = (OnLifecycleEvent) method.getAnnotation(OnLifecycleEvent.class);
            if (onLifecycleEvent != null) {
                Class[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length <= 0) {
                    i = 0;
                } else if (parameterTypes[0].isAssignableFrom(LifecycleOwner.class)) {
                    i = 1;
                } else {
                    throw new IllegalArgumentException("invalid parameter type. Must be one and instanceof LifecycleOwner");
                }
                Event value = onLifecycleEvent.value();
                if (parameterTypes.length > 1) {
                    if (!parameterTypes[1].isAssignableFrom(Event.class)) {
                        throw new IllegalArgumentException("invalid parameter type. second arg must be an event");
                    } else if (value == Event.ON_ANY) {
                        i = 2;
                    } else {
                        throw new IllegalArgumentException("Second arg is supported only for ON_ANY value");
                    }
                }
                if (parameterTypes.length <= 2) {
                    verifyAndPutHandler(hashMap, new MethodReference(i, method), value, cls);
                } else {
                    throw new IllegalArgumentException("cannot have more than 2 params");
                }
            }
        }
        CallbackInfo callbackInfo = new CallbackInfo(hashMap);
        sInfoCache.put(cls, callbackInfo);
        return callbackInfo;
    }
}
