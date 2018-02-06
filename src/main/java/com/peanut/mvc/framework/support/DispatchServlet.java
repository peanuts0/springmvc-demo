package com.peanut.mvc.framework.support;

import com.peanut.mvc.framework.container.BeanContainer;
import com.peanut.mvc.framework.container.MappingContainer;
import com.peanut.mvc.framework.enums.RequestMethod;
import com.peanut.mvc.framework.mapping.RequestMappingHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 中心控制器
 *
 * @author peanut.huang
 * @date 2018/2/4.
 */
@WebServlet(urlPatterns = "/*",loadOnStartup = 0)
public class DispatchServlet extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(DispatchServlet.class);

    /**
     * 请求处理
     *
     * @param req
     * @param resp
     */
    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {


        String uri = req.getRequestURI();

        String requestMethod = req.getMethod();

        RequestMappingHandle handle = MappingContainer.getMapping(uri);
        if(handle == null){
            String result = "{'data':null,'success':false,'msg':'this url is not mapping'}";
            PrintWriter writer = resp.getWriter();
            writer.write(result);
            writer.flush();
            return;
        }


        RequestMethod [] requestMethods = handle.getRequestMethods();
        RequestMethod rm = Arrays.stream(requestMethods).filter( m -> m.toString().equalsIgnoreCase(requestMethod)).findFirst().orElse(null);
        if(rm == null){
            String result = "{'data':null,'success':false,'msg':'request method is invalid'}";
            PrintWriter writer = resp.getWriter();
            writer.write(result);
            writer.flush();
            return;
        }



        Class ctrlClass = handle.getCtlClass();
        Method method = handle.getMethod();

        //
        Object object = BeanContainer.getBean(ctrlClass);

        Object result = null;
        try {
           result = method.invoke(object);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        PrintWriter writer = resp.getWriter();
        writer.write(result.toString());
        writer.flush();


    }

}
