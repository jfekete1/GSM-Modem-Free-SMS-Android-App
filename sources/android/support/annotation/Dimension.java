package android.support.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface Dimension {

    /* renamed from: DP */
    public static final int f0DP = 0;

    /* renamed from: PX */
    public static final int f1PX = 1;

    /* renamed from: SP */
    public static final int f2SP = 2;

    int unit() default 1;
}
