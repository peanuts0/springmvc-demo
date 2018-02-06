package com.peanut.mvc.framework.support;

import com.peanut.mvc.framework.annotation.Controller;
import com.peanut.mvc.framework.annotation.RequestMapping;
import com.peanut.mvc.framework.container.BeanContainer;
import com.peanut.mvc.framework.container.MappingContainer;
import com.peanut.mvc.framework.enums.RequestMethod;
import com.peanut.mvc.framework.mapping.RequestMappingHandle;
import com.peanut.mvc.framework.utils.ClassUtils;
import com.peanut.mvc.framework.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * ServletContext 容器初始化时初始化项目中的bean
 *
 * @author peanut.huang
 * @date 2018/2/4.
 */
@WebListener
public class Loader implements ServletContextListener {


    private static final Logger LOGGER = LoggerFactory.getLogger(Loader.class);

    /**
     * 基础包路径
     */
    public final  String                       basePackage           = "com.peanut";

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        LOGGER.info("loader init。。。");

        /**
         *
         * 1、加载所有 service与controller 注解的类
         *
         * 2、实例化类
         *
         * 3、ioc 注入
         *
         * 4、处理request Mapping
         */

        //找到所有的bean类(有service 和 controller注解的类)
        List<Class> allClassList = ClassUtils.loadBeanClasses(basePackage);

        //实例化类，并放入bean容器
        initClass(allClassList);

        //ioc
        initIoc();

        // 处理 mapping mapping
        handleRequest(allClassList);
    }

    /**
     * 处理请求
     *
     * @param allClassList
     */
    private void handleRequest(List<Class> allClassList) {

        //
        allClassList.stream()
                    //过滤出controller类
                    .filter(e -> e.isAnnotationPresent(Controller.class))
                    .collect(Collectors.toList())

                    //依次处理
                    .forEach(this::handleCtrl);
    }

    /**
     * 处理ctl
     * @param ctl
     */
    private void handleCtrl(Class ctl) {

        RequestMapping ctlMapping = (RequestMapping) ctl.getAnnotation(RequestMapping.class);

        //请求头地址
        String ctlPath = ctlMapping.value();

        Arrays.stream(ctl.getMethods())

              //过滤方法上有requestMapping 注解
              .filter(m -> m.isAnnotationPresent(RequestMapping.class))
              .collect(Collectors.toList())

              //各个方法逐个处理
              .forEach(method -> {


                  RequestMapping methodMapping = method.getAnnotation(RequestMapping.class);

                  RequestMethod [] requestMethods = methodMapping.method();
                  String methodUrl = methodMapping.value();

                  //方法最终的请求路径
                  String resultUrl = handleUrl(ctlPath, methodUrl);


                  RequestMappingHandle requestMappingHandle = new RequestMappingHandle();
                  requestMappingHandle.setCtlClass(ctl);
                  requestMappingHandle.setRequestMethods(requestMethods);
                  requestMappingHandle.setRequestUrl(resultUrl);
                  requestMappingHandle.setMethod(method);

                  // 放入容器
                  MappingContainer.setMapping(resultUrl, requestMappingHandle);

              });

    }

    private String handleUrl(String ctlPath, String methodUrl) {

        if(StringUtils.isNotEmpty(ctlPath)){

            if(!ctlPath.startsWith("/")){
                ctlPath = "/" + ctlPath;
            }
            if(!ctlPath.endsWith("/")){
                ctlPath =  ctlPath + "/";
            }
        }

        if(StringUtils.isNotEmpty(methodUrl)){

            if(methodUrl.startsWith("/")){
                methodUrl = methodUrl.replaceFirst("/","");
            }
            if(methodUrl.endsWith("/")){
                methodUrl =  methodUrl.substring(0, methodUrl.length() -1);
            }
        }
        return ctlPath + methodUrl;
    }


    /**
     * ioc
     */
    private void initIoc() {

        ConcurrentMap<Class, Object> beanContainer = BeanContainer.getBeanContainer();

        beanContainer.forEach( (clazz, object) ->{

            Field[] fields = clazz.getDeclaredFields();

            Arrays.stream(fields)
                  // 过滤出有resource注入
                  .filter(field -> field.isAnnotationPresent(Resource.class))
                  .collect(Collectors.toList())
                  //依次注入属性
                  .forEach(iField ->{

                      //当前注入的field 类
                      Class fieldClass = iField.getType();

                      //获取注入field类实例
                      Object fieldInstance = BeanContainer.getBean(fieldClass);

                      iField.setAccessible(true);

                      try {
                          iField.set(object, fieldInstance);
                      } catch (IllegalAccessException e) {
                          e.printStackTrace();
                      }
                  });
        });

    }

    /**
     *
     * @param allClassList
     */
    private void initClass(List<Class> allClassList) {

       allClassList.forEach(clazz -> {

           try {
                Object object = clazz.newInstance();

                Class [] interfaceClasses = clazz.getInterfaces();

                //接口取第一个实现简单处理下....
                if(interfaceClasses != null && interfaceClasses.length > 0){
                    BeanContainer.setBean(interfaceClasses[0], object);
                }else {

                    BeanContainer.setBean(clazz, object);
                }

           } catch (InstantiationException | IllegalAccessException e) {
               e.printStackTrace();
           }
       });
    }
}
