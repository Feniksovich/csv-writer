package org.writer.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class ReflectionUtils {

    public static List<Field> getAnnotatedFields(Class<? extends Annotation> annotation, Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(annotation))
                .toList();
    }

    public static Object getFieldValue(Object obj, Field field) {
        try {
            field.setAccessible(true);
            return field.get(obj);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(field.getName(), ex);
        }
    }

}
