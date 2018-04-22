package com.smart4j.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by hzdmm123 on 2018/4/22.
 */
public final class CodeUtil {
    private static final Logger logger = LoggerFactory.getLogger(CodeUtil.class);
    //url 编码
    public static String encodeURL(String source){
        String target;
        try{
            target = URLEncoder.encode(source,"UTF-8");
        }catch (Exception e){
            logger.error("encode url failure",e);
            throw new RuntimeException(e);
        }
        return target;
    }
    //url 解码
    public static String decodeURL(String source){
        String target;
        try{
            target = URLDecoder.decode(source,"UTF-8");
        }catch (Exception e){
            logger.error("encode url failure",e);
            throw new RuntimeException(e);
        }
        return target;
    }
}
