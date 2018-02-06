package com.peanut.mvc.framework.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.peanut.mvc.framework.annotation.Controller;
import com.peanut.mvc.framework.annotation.Service;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * class 工具类
 *
 * @author peanut.huang
 * @date 2018/2/4.
 */
public class ClassUtils {

    /**
     * 类限定名与类map
     */
    public static ConcurrentMap<String, Class> classMap = Maps.newConcurrentMap();


    /**
     * 类限定名获取类
     *
     * @param className 类全限定名，例：com.peanut.controller.WebTest
     * @return
     */
    public static Class findByClassName(String className){
        if(StringUtils.isEmpty(className)){
            return null;
        }

        return classMap.get(className);
    }



    /**
     * 加载包路径下 有@Service 和 @Controller 注解的类
     *
     * @param packageName
     * @return
     */
    public static List<Class> loadBeanClasses(String packageName) {
        if(packageName == null || "".equals(packageName.trim())){
            throw new IllegalArgumentException("包名为空");
        }

        List<Class> result = Lists.newArrayList();

       ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        try {
            
            Enumeration<URL> urls = classLoader.getResources(packageName.replace(".","/"));
            while (urls.hasMoreElements()){
                URL url = urls.nextElement();

                String protocol = url.getProtocol();

                String filePath = url.getPath();

                if("file".equals(protocol)){

                    findClass(result, filePath, packageName);

                    //第三方jar依赖
                }else if("jar".equals(protocol)){

                    findThirdClass(result, packageName, url);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(result);

        return result;
    }

    private static void findThirdClass(List<Class> result, String packageName, URL url) {

        try {
            JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();

            JarFile jarFile = jarURLConnection.getJarFile();

            Enumeration<JarEntry> jarEntries = jarFile.entries();
            while (jarEntries.hasMoreElements()) {

                JarEntry jarEntry = jarEntries.nextElement();

                // 类似：sun/security/internal/interfaces/TlsMasterSecret.class
                String jarEntryName = jarEntry.getName();

                String clazzName = jarEntryName.replace("/", ".");

                int endIndex = clazzName.lastIndexOf('.');
                String prefix = null;
                if (endIndex > 0) {
                    String prefix_name = clazzName.substring(0, endIndex);
                    if(clazzName.endsWith(".class")) {
                        clazzName = prefix_name;
                    }
                    endIndex = prefix_name.lastIndexOf('.');
                    if(endIndex > 0){
                        prefix = prefix_name.substring(0, endIndex);
                    }
                }
                if (prefix != null && jarEntryName.endsWith(".class")) {

                    addClass(result, clazzName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void findClass(List<Class> result, String filePath, String packageName) {

      File[] files = new File(filePath).listFiles(
                                                    //接受目录或者以 .class结尾的文件
                                                    (file) -> file.isDirectory()
                                                    ||
                                                    (file.isFile() && file.getName().endsWith(".class"))
                                                );

      if(files != null){
          Arrays.stream(files).forEach(file -> {
             String fileName = file.getName();

             //文件
             if(file.isFile()){

                 String className = getClassName(fileName, packageName);

                 addClass(result, className);

             }else{
                //目录,递归处理
                 
                 String subFilePath = filePath + "/" + fileName;

                 String subPkgName = packageName + "." + fileName;

                 findClass(result, subFilePath, subPkgName);
             }
          });
      }
    }

    private static void addClass(List<Class> result, String className) {

        try {
            Class clazz = Class.forName(className);
            // 加载 service和controller 注解类
            if(clazz.isAnnotationPresent(Service.class) || clazz.isAnnotationPresent(Controller.class)){

                result.add(clazz);

                //进map
                classMap.putIfAbsent(className, clazz);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static String getClassName(String fileName, String packageName) {

        int index = fileName.lastIndexOf(".");
        String clazz = fileName.substring(0, index);

        return packageName + "." + clazz;
    }
}
