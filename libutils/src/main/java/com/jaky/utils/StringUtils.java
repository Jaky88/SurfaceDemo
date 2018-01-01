package com.jaky.utils;

import java.util.Iterator;

/**
 * Created by Jack on 2017/12/31.
 */

public class StringUtils {
    public static String join(Iterable<?> elements, String delimiter) {
        StringBuilder sb = new StringBuilder();

        Object e;
        for (Iterator var3 = elements.iterator(); var3.hasNext(); sb.append(e)) {
            e = var3.next();
            if (sb.length() > 0) {
                sb.append(delimiter);
            }
        }

        return sb.toString();
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.equals("");
    }

    public static boolean isNotBlank(String s) {
        return s != null && s.length() > 0;
    }

    public static int countOccurrencesOf(String string, String pattern) {
        int lastIndex = 0;
        int count = 0;

        while (lastIndex != -1) {
            lastIndex = string.indexOf(pattern, lastIndex);
            if (lastIndex != -1) {
                ++count;
                lastIndex += pattern.length();
            }
        }

        return count;
    }
}
