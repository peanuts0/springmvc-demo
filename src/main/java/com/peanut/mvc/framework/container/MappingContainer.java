package com.peanut.mvc.framework.container;

import com.google.common.collect.Maps;
import com.peanut.mvc.framework.mapping.RequestMappingHandle;

import java.util.concurrent.ConcurrentMap;

/**
 * @author peanut.huang
 * @date 2018/2/6.
 */
public class MappingContainer {

    private  static ConcurrentMap<String, RequestMappingHandle> mappingContainer = Maps.newConcurrentMap();

    /**
     * 获取mapping 容器
     *
     * @return
     */
    public static ConcurrentMap getMappingContainer(){
        return mappingContainer;
    }

    /**
     * 获取单个请求handle
     *
     * @param requetUrl
     * @return
     */
    public static RequestMappingHandle getMapping(String requetUrl){
        return mappingContainer.get(requetUrl);
    }


    /**
     * 设置单个请求handle
     *
     * @param requetUrl
     * @param handle
     * @return
     */
    public static void setMapping(String requetUrl, RequestMappingHandle handle){
        mappingContainer.put(requetUrl, handle);
    }


}
