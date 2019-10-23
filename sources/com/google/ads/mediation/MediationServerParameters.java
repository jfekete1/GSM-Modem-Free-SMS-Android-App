package com.google.ads.mediation;

import com.google.android.gms.internal.ads.zzbbd;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

@Deprecated
public class MediationServerParameters {

    public static final class MappingException extends Exception {
        public MappingException(String str) {
            super(str);
        }
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Parameter {
        String name();

        boolean required() default true;
    }

    public void load(Map<String, String> map) throws MappingException {
        Field[] fields;
        HashMap hashMap = new HashMap();
        for (Field field : getClass().getFields()) {
            Parameter parameter = (Parameter) field.getAnnotation(Parameter.class);
            if (parameter != null) {
                hashMap.put(parameter.name(), field);
            }
        }
        if (hashMap.isEmpty()) {
            zzbbd.zzeo("No server options fields detected. To suppress this message either add a field with the @Parameter annotation, or override the load() method.");
        }
        for (Entry entry : map.entrySet()) {
            Field field2 = (Field) hashMap.remove(entry.getKey());
            if (field2 != null) {
                try {
                    field2.set(this, entry.getValue());
                } catch (IllegalAccessException unused) {
                    String str = (String) entry.getKey();
                    StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 49);
                    sb.append("Server option \"");
                    sb.append(str);
                    sb.append("\" could not be set: Illegal Access");
                    zzbbd.zzeo(sb.toString());
                } catch (IllegalArgumentException unused2) {
                    String str2 = (String) entry.getKey();
                    StringBuilder sb2 = new StringBuilder(String.valueOf(str2).length() + 43);
                    sb2.append("Server option \"");
                    sb2.append(str2);
                    sb2.append("\" could not be set: Bad Type");
                    zzbbd.zzeo(sb2.toString());
                }
            } else {
                String str3 = (String) entry.getKey();
                String str4 = (String) entry.getValue();
                StringBuilder sb3 = new StringBuilder(String.valueOf(str3).length() + 31 + String.valueOf(str4).length());
                sb3.append("Unexpected server option: ");
                sb3.append(str3);
                sb3.append(" = \"");
                sb3.append(str4);
                sb3.append("\"");
                zzbbd.zzdn(sb3.toString());
            }
        }
        StringBuilder sb4 = new StringBuilder();
        for (Field field3 : hashMap.values()) {
            if (((Parameter) field3.getAnnotation(Parameter.class)).required()) {
                String str5 = "Required server option missing: ";
                String valueOf = String.valueOf(((Parameter) field3.getAnnotation(Parameter.class)).name());
                zzbbd.zzeo(valueOf.length() != 0 ? str5.concat(valueOf) : new String(str5));
                if (sb4.length() > 0) {
                    sb4.append(", ");
                }
                sb4.append(((Parameter) field3.getAnnotation(Parameter.class)).name());
            }
        }
        if (sb4.length() > 0) {
            String str6 = "Required server option(s) missing: ";
            String valueOf2 = String.valueOf(sb4.toString());
            throw new MappingException(valueOf2.length() != 0 ? str6.concat(valueOf2) : new String(str6));
        }
    }
}
