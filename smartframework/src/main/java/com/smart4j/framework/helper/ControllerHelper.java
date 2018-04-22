package com.smart4j.framework.helper;

import com.smart4j.framework.annotation.Action;
import com.smart4j.framework.bean.Handler;
import com.smart4j.framework.bean.Request;
import com.smart4j.framework.util.CollectionUtil;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by hzdmm123 on 2018/4/22.
 */
public final class ControllerHelper {
    //请求与处理器的映射关系
    private static final Map<Request,Handler> ACTION_MAP = new HashMap<Request, Handler>();

    //

    static {
        //获取所有的controller类型
        Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();

        if (CollectionUtil.isNotEmpty(controllerClassSet)){
            for (Class<?> controllerClass : controllerClassSet){
//                遍历controller类型
                Method[] methods = controllerClass.getDeclaredMethods();
                //获取controller定义的方法
                if (ArrayUtils.isNotEmpty(methods)){
                    for (Method method : methods){
                        //判断方法上是否有注解
                        if (method.isAnnotationPresent(Action.class)){
                            Action action = method.getAnnotation(Action.class);
                            String mapping = action.value();
                            //验证URL映射规则
                            if (mapping.matches("\\w+:/\\w*")){
                                String[] array = mapping.split(":");
                                if (ArrayUtils.isNotEmpty(array)&&array.length==2){
//                                    获取请求方法和路径
                                    String requestMethod = array[0];
                                    String requestPath = array[1];
                                    Request request = new Request(requestMethod,requestPath);
                                    Handler handler = new Handler(controllerClass,method);
                                    ACTION_MAP.put(request,handler);
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    //获取handler
    public static Handler getHandler(String requestMethod,String requestpath){
        Request request = new Request(requestMethod,requestpath);
        return ACTION_MAP.get(request);
    }

}
