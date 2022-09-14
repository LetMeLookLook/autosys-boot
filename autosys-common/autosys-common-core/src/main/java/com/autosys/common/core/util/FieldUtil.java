package com.autosys.common.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description 驼峰于下划线字段转换工具类
 * @author jingqiu.wang
 * @date 2022年8月29日 16点34分
 */
public class FieldUtil {
    private FieldUtil() {
    }

    /**
    * 从驼峰转为下划线字段
    */
    public static String convertFromCamel(String fromCamel){
        Pattern humpPattern = Pattern.compile("[A-Z]");
        Matcher matcher = humpPattern.matcher(fromCamel);
        StringBuffer sb = new StringBuffer();
        while(matcher.find()){
            matcher.appendReplacement(sb, "_"+matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
    /**
    *  从下划线转为驼峰字段
    */
    public static String convertToCamel(String underscore){
        String[] ss = underscore.split("_");
        if(ss.length ==1){
            return underscore;
        }

        StringBuffer sb = new StringBuffer();
        sb.append(ss[0]);
        for (int i = 1; i < ss.length; i++) {
            sb.append(upperFirstCase(ss[i]));
        }

        return sb.toString();
    }
    /**
    * @description: 首字母大写
    */
    private static String upperFirstCase(String str) {
        char[] chars = str.toCharArray();
        chars[0] -= 32;
        return String.valueOf(chars);
    }
}
