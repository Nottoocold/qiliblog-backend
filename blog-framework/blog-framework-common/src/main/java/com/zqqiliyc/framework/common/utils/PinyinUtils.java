package com.zqqiliyc.framework.common.utils;

import com.github.promeg.pinyinhelper.Pinyin;

import java.util.stream.Collectors;

/**
 * @author qili
 * @date 2025-11-23
 */
public class PinyinUtils {

    public static boolean isChinese(char c) {
        return Pinyin.isChinese(c);
    }

    public static boolean isChinese(String s) {
        if (s.isBlank()) {
            return false;
        }
        return s.chars().allMatch(c -> isChinese((char) c));
    }

    public static String toPinyin(char c) {
        String pinyin = Pinyin.toPinyin(c);
        return pinyin.isEmpty() ? "" : pinyin.toLowerCase();
    }

    public static String toPinyin(String s) {
        return toPinyin(s, "");
    }

    public static String toPinyin(String s, String separator) {
        if (s.isBlank()) {
            return "";
        }
        String pinyin = Pinyin.toPinyin(s, separator);
        return pinyin.isEmpty() ? "" : pinyin.toLowerCase();
    }

    /**
     * 获取拼音首字母
     *
     * @param c         字符
     * @param upperCase 首字母是否大写
     * @return 拼音首字母
     */
    public static String getFirstLetter(char c, boolean upperCase) {
        String cc = toPinyin(c);
        return cc.isEmpty() ? "" : upperCase ? cc.substring(0, 1).toUpperCase() : cc.substring(0, 1);
    }

    /**
     * 获取拼音首字母, 如"张三"，则返回"ZS"or"zs"
     *
     * @param s         字符串
     * @param upperCase 首字母是否大写
     * @return 拼音首字母
     */
    public static String getFirstLetter(String s, boolean upperCase) {
        if (s.isBlank()) {
            return "";
        }
        return s.chars().mapToObj(c -> getFirstLetter((char) c, upperCase)).collect(Collectors.joining());
    }
}
