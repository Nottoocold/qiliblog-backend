package com.zqqiliyc.framework.web.generate;

import cn.hutool.core.util.RandomUtil;

import java.util.function.Function;

/**
 * 虚拟手机号生成器
 *
 * @author qili
 * @date 2025-07-12
 */
public class VirtualPhoneGenerator {
    // 常见的虚拟运营商号段
    private static final String[] VIRTUAL_PREFIXES = {
            "170", "171", "165", "167",
            "162", "174", "140", "144",
            "146", "148", "149"
    };

    /**
     * 生成一个随机的虚拟手机号
     *
     * @return 11位虚拟手机号字符串
     */
    public static String generate() {
        return generate(candidate -> false);
    }

    /**
     * 生成一个随机的虚拟手机号，如果生成的号码已经存在则重新生成
     *
     * @param uniqueCheck 用于检查号码是否重复的函数,如果号码重复返回true，否则false
     * @return 11位虚拟手机号字符串
     */
    public static String generate(Function<String, Boolean> uniqueCheck) {
        String phoneNumber;
        do {
            // 随机选择一个虚拟号段
            String prefix = VIRTUAL_PREFIXES[RandomUtil.randomInt(VIRTUAL_PREFIXES.length)];

            // 生成后8位随机数字
            StringBuilder suffix = new StringBuilder();
            for (int i = 0; i < 8; i++) {
                suffix.append(RandomUtil.randomInt(10));
            }

            phoneNumber = prefix + suffix;
        } while (uniqueCheck.apply(phoneNumber)); // 确保不重复
        return phoneNumber;
    }

    /**
     * 批量生成虚拟手机号
     *
     * @param count 要生成的号码数量
     * @return 手机号数组
     */
    public static String[] generateBatch(int count) {
        return generateBatch(count, candidate -> false);
    }

    public static String[] generateBatch(int count, Function<String, Boolean> uniqueCheck) {
        if (count <= 0) {
            throw new IllegalArgumentException("Count must be positive");
        }

        String[] numbers = new String[count];
        for (int i = 0; i < count; i++) {
            numbers[i] = generate(uniqueCheck);
        }
        return numbers;
    }

    /**
     * 验证是否为虚拟运营商号码
     *
     * @param phoneNumber 要验证的手机号
     * @return 如果是虚拟运营商号码返回true，否则false
     */
    public static boolean isVirtualPhone(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.length() != 11) {
            return false;
        }

        String prefix = phoneNumber.substring(0, 3);
        for (String virtualPrefix : VIRTUAL_PREFIXES) {
            if (virtualPrefix.equals(prefix)) {
                return true;
            }
        }
        return false;
    }
}