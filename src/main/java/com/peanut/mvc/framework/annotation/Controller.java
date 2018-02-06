package com.peanut.mvc.framework.annotation;

import java.lang.annotation.*;

/**
 * controller
 *
 * @author peanut.huang
 * @date 2018/2/4.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Controller {

    /**
     * 拦截路径
     *
     * @return
     */
    String value() default "";
}
