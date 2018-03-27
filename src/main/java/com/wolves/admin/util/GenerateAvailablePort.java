package com.wolves.admin.util;


import java.util.Set;

/**
 * zhanghuan
 */
public class GenerateAvailablePort {
    // 最小端口限制
    public static final int MIN_LIMIT = 1;
    // 最大端口限制
    public static final int MAX_LIMIT = 65535;

    /**
     * 生成从min（包含）到max（包含）之间的一个随机端口
     * 该方法已经通过测试
     *
     * @param min      最小值（包含）
     * @param max      最大值（包含）
     * @param excludes 排除的端口
     * @return 随机端口
     */
    public static int generate(int min, int max, Set<String> excludes) {
        if (min > max) {
            int backup = min;
            min = max;
            max = backup;
        }
        min = min < MIN_LIMIT ? MIN_LIMIT : min;
        max = max > MAX_LIMIT ? MAX_LIMIT : max;
        for (int i = min; i <= max; i++) {
            if (!excludes.contains(i + "")) {
                return i;
            }
        }
        return 0;
    }

    /**
     * 生成从min（包含）到max（包含）之间的一个随机端口
     * 该方法已经通过测试
     *
     * @param minStr 最小值（包含）
     * @param maxStr 最大值（包含）
     * @return 随机端口
     */
    public static int generate(String minStr, String maxStr, Set<String> excludes) {
        int min = Integer.parseInt(minStr);
        int max = Integer.parseInt(maxStr);
        return generate(min, max, excludes);
    }

}
