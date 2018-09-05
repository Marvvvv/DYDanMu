package com.yukari.utils;

import org.apache.commons.lang3.StringUtils;

public class DYSerializeUtil {

    private static final String SLASH = "@S";   //  "/"
    private static final String AT = "@A";      // "@"

    private static final String HEAD_ICON_URL_PREFIX = "https://apic.douyucdn.cn/upload/";  // 头像url前缀
    private static final String HEAD_ICON_URL_SUFFIX = "_middle.jpg";  // 头像url后缀


    /**
     * 将进房消息中的头像信息生成url
     * @param iconUrl 头像信息
     * @return
     */
    public static String headIconUrlEscape (String iconUrl) {
        return HEAD_ICON_URL_PREFIX + StringUtils.replace(iconUrl,SLASH,"/") + HEAD_ICON_URL_SUFFIX;
    }



}
