package com.smart4j.framework;

import com.smart4j.framework.bean.Data;
import com.smart4j.framework.bean.Handler;
import com.smart4j.framework.bean.Param;
import com.smart4j.framework.bean.View;
import com.smart4j.framework.helper.BeanHelper;
import com.smart4j.framework.helper.ConfigHelper;
import com.smart4j.framework.helper.ControllerHelper;
import com.smart4j.framework.util.*;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hzdmm123 on 2018/4/22.
 */
@WebServlet(urlPatterns = "/*",loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {

    @Override
    public void init(ServletConfig servletConfig) throws ServletException{

        //初始化相关的helper类
        HelperLoader.init();
        //获取servletContext对象
        ServletContext servletContext = servletConfig.getServletContext();
        //注册处理Jsp的servlet
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigHelper.getAppJspPath()+"*");

        //注册处理静态资源的默认servlet
        ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
        defaultServlet.addMapping(ConfigHelper.getAppAssetPath()+"*");
    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
        //获取请求方法和请求路径
        String requestMethod = request.getMethod().toLowerCase();
        String requestpath = request.getPathInfo();

        //获取处理Action
        Handler handler = ControllerHelper.getHandler(requestMethod,requestpath);

        if (handler != null){
            Class<?> controllerClass = handler.getControllerClass();
            Object controllerBean = BeanHelper.getBean(controllerClass);

            //创建请求参数对象
            Map<String,Object> paramMap = new HashMap<String, Object>();
            Enumeration<String> paramNames = request.getParameterNames();

            while (paramNames.hasMoreElements()){
                String paramName = paramNames.nextElement();
                String paramValue = request.getParameter(paramName);
                paramMap.put(paramName,paramValue);
            }

            String body = CodeUtil.decodeURL(StreamUtil.getString(request.getInputStream()));
            if (StringUtil.isNotEmpty(body)){
                String[] params = StringUtils.split(body,"&");
                if (ArrayUtils.isNotEmpty(params)){
                    for (String param : params){
                        String[] array = StringUtils.split(param,"=");
                            if (ArrayUtils.isNotEmpty(array)&&array.length==2){
                                String paramName = array[0];
                                String paramValue = array[1];
                                paramMap.put(paramName,paramValue);
                            }

                    }
                }
            }

            Param param = new Param(paramMap);

            Method actionMethod = handler.getActionMethod();

            Object result = ReflectionUtil.invokeMethod(controllerBean,actionMethod,param);

            //处理action的返回值

            if (result instanceof View){
                View view = (View) result;
                String path = view.getPath();
                if (StringUtil.isNotEmpty(path)){
                    if (path.startsWith("/")){
                        response.sendRedirect(request.getContextPath() + path);
                    }else {
                        Map<String,Object> model = view.getModel();
                        for (Map.Entry<String,Object> entry : model.entrySet()){
                            request.setAttribute(entry.getKey(),entry.getValue());
                        }

                        request.getRequestDispatcher(ConfigHelper.getAppJspPath()+path).forward(request,response);
                    }
                }
            }else if (result instanceof Data){
                //返回Json数据
                Data data = (Data) result;
                Object model = data.getModel();
                if (model != null){
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    PrintWriter writer = response.getWriter();
                    String json = JsonUtil.toJson(model);
                    writer.write(json);
                    writer.flush();
                    writer.close();
                }
            }

        }
    }
}
