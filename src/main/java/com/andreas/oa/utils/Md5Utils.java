package com.andreas.oa.utils;

import org.apache.commons.codec.digest.DigestUtils;


/**
 * @Author andreaszhou
 * @ClassName Md5Utils
 * @Description 加密工具类--对密码进行加密，防止被盗的风险
 * @date 2021/1/21 11:26
 * @Version 1.0
 */
public class Md5Utils {
    public static String md5Digest(String resource, int salt){
        char[] chars = resource.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) (chars[i] + salt);
        }
        String target = new String(chars);
        String md5Hex = DigestUtils.md5Hex(target);
        return md5Hex;
    }
}
