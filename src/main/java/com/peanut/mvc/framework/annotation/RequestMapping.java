package com.peanut.mvc.framework.annotation;


import com.peanut.mvc.framework.enums.RequestMethod;

import java.lang.annotation.*;

/**
 * mapping
 *
 * @author peanut.huang
 * @date 2018/2/4.
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {

    /**
     * 请求路径
     *
     * @return
     */
    String value() default "";

    /**
     * 请求方法
     *
     * @return
     */
    RequestMethod[] method() default {};
}
