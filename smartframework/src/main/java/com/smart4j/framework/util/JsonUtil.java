package com.smart4j.framework.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by hzdmm123 on 2018/4/22.
 */
public final class JsonUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    //pojo转为json
    public static <T> String toJson(T obj){
        String json;

        try {
            json = OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            LOGGER.error("convert POJO to json failure",e);
            throw  new RuntimeException(e);
        }

        return json;
    }

    //json 转pojo
    public static <T> T fromJson(String json,Class<T> type){
        T pojo;
        try{
            pojo = OBJECT_MAPPER.readValue(json,type);
        }catch (Exception e){
            LOGGER.error("convert json to POJO failure",e);
            throw  new RuntimeException(e);
        }
        return pojo;
    }
}
