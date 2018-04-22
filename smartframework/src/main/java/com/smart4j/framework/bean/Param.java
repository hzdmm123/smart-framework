package com.smart4j.framework.bean;

import com.smart4j.framework.util.CastUtil;

import java.util.Map;

/**
 * Created by hzdmm123 on 2018/4/22.
 */
public class Param {
    private Map<String,Object> paramMap;

    public Param(Map<String,Object> paramMap){
        this.paramMap = paramMap;
    }

    //根据参数获取long类型
    public long getLong(String name){
        return CastUtil.castLong(paramMap.get(name));
    }


    //获取所有字段的信息
    public Map<String,Object> getMap(){
        return paramMap;
    }
}
