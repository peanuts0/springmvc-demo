package com.peanut.mvc.framework.container;

import com.google.common.collect.Maps;

import java.util.concurrent.ConcurrentMap;

/**
 * @author peanut.huang
 * @date 2018/2/6.
 */
public class BeanContainer {

     private  static ConcurrentMap<Class, Object>  beanContainer = Maps.newConcurrentMap();

    /**
     * 获取所有bean
     *
     * @return
     */
     public static ConcurrentMap<Class, Object> getBeanContainer(){
         return beanContainer;
     }

    /**
     * 获取bean 实例
     *
     * @param className
     * @return
     */
     public static Object getBean(Class className){
         return beanContainer.get(className);
     }


    /**
     * 设置 container 实例
     *
     * @param className
     * @return
     */
    public static void setBean(Class className, Object object){
        beanContainer.put(className, object);
    }


}
