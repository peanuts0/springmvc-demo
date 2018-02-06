package com.peanut.mvc.framework.utils;

/**
 * @author peanut.huang
 * @date 2018/2/6.
 */
public class StringUtils {



    /**
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }

    /**
     * 字符是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str){
        return str == null || "".equals(str.trim());
    }
}
