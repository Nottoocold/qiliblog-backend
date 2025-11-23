package com.zqqiliyc.framework.common.utils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author qili
 * @date 2025-11-23
 */
public class PinyinUtilsTest {

    @Test
    public void testIsChinese() {
        assertTrue(PinyinUtils.isChinese('中'));
        assertFalse(PinyinUtils.isChinese('a'));
        assertFalse(PinyinUtils.isChinese(' '));
        assertFalse(PinyinUtils.isChinese('\n'));
        assertTrue(PinyinUtils.isChinese("中文"));
        assertFalse(PinyinUtils.isChinese("中文2131"));
    }

    @Test
    public void testToPinyin() {
        assertEquals("zhong", PinyinUtils.toPinyin('中'));
        assertEquals("wangsan", PinyinUtils.toPinyin("王三"));
    }

    @Test
    public void testToPinyinFirstLetter() {
        assertEquals("z", PinyinUtils.getFirstLetter('中', false));
        assertEquals("Z", PinyinUtils.getFirstLetter('中', true));
        assertEquals("ww", PinyinUtils.getFirstLetter("王五", false));
        assertEquals("WW", PinyinUtils.getFirstLetter("王五", true));
        assertEquals("jf", PinyinUtils.getFirstLetter("激发", false));
        assertEquals("JF", PinyinUtils.getFirstLetter("姬发", true));
    }
}