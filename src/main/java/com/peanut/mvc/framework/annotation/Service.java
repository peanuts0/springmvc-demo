package com.peanut.mvc.framework.annotation;

import java.lang.annotation.*;

/**
 * service
 *
 * @author peanut.huang
 * @date 2018/2/4.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Service {

    /**
     * service name
     *
     * @return
     */
    String value() default "";
}
