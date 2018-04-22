package com.smart4j.framework.helper;

import com.smart4j.framework.util.ReflectionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by hzdmm123 on 2018/4/22.
 */
public final class BeanHelper {
    private static final Map<Class<?>,Object> BEAN_MAP = new HashMap<Class<?>, Object>();
    //定义Bean映射
    static {
        Set<Class<?>> beanClassSet = ClassHelper.getBeanClassSet();
        for (Class<?> beanClass : beanClassSet){
            Object obj = ReflectionUtil.newInstance(beanClass);
            BEAN_MAP.put(beanClass,obj);
        }
    }

    //获取bean映射
    public static Map<Class<?>,Object> getBeanMap(){
        return BEAN_MAP;
    }

    //获取bean实例
    public static <T> T getBean(Class<T> cls){
        if (!BEAN_MAP.containsKey(cls)){
            throw new RuntimeException("can not get bean by class"+cls);
        }

        return (T) BEAN_MAP.get(cls);
    }
}
