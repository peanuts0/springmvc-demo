package com.peanut.mvc.framework.mapping;

import com.peanut.mvc.framework.enums.RequestMethod;

import java.lang.reflect.Method;

/**
 * @author peanut.huang
 * @date 2018/2/6.
 */
public class RequestMappingHandle {

    /**
     * 请求ctr
     */
    private Class ctlClass;

    /**
     * 请求方法
     */
    private RequestMethod[] requestMethods;

    /**
     * 映射到ctrl的方法
     */
    private Method method;

    /**
     * 请求地址
     */
    private String requestUrl;

    public Class getCtlClass() {
        return ctlClass;
    }

    public void setCtlClass(Class ctlClass) {
        this.ctlClass = ctlClass;
    }

    public RequestMethod[] getRequestMethods() {
        return requestMethods;
    }

    public void setRequestMethods(RequestMethod[] requestMethods) {
        this.requestMethods = requestMethods;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
