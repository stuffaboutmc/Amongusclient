package com.user.utilitymod.util;

import java.lang.reflect.Field;

/**
 * A few fields in 1.8.9's deobfuscated Minecraft classes are private even
 * though they're freely read/written by basically every utility mod for this
 * version. Reflection is the standard, simple workaround (the alternative is
 * an access-transformer config, which is more setup for the same result).
 */
public class ReflectionUtil {

    public static void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object getField(Object target, String fieldName) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(target);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
