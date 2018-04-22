package com.smart4j.framework;

import com.smart4j.framework.helper.BeanHelper;
import com.smart4j.framework.helper.ClassHelper;
import com.smart4j.framework.helper.ControllerHelper;
import com.smart4j.framework.helper.IocHelper;
import com.smart4j.framework.util.ClassUtil;

/**
 * Created by hzdmm123 on 2018/4/22.
 */
public final class HelperLoader {
    public static void init(){
        Class<?>[] classList = {ClassHelper.class, BeanHelper.class, IocHelper.class, ControllerHelper.class};
        for (Class<?> cls : classList){
            ClassUtil.loadClass(cls.getName(),true);
        }
    }
}
