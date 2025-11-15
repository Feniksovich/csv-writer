package org.writer.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * Утилитарный класс для работы с Reflection API.
 */
public class ReflectionUtils {

    /**
     * Получает список всех полей класса, помеченных указанной аннотацией.
     *
     * @param annotation класс аннотации для поиска
     * @param clazz класс, поля которого необходимо проверить
     * @return список полей, помеченных указанной аннотацией
     */
    public static List<Field> getAnnotatedFields(Class<? extends Annotation> annotation, Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(annotation))
                .toList();
    }

    /**
     * Получает значение поля объекта.
     *
     * @param obj объект, из которого извлекается значение поля
     * @param field поле, значение которого необходимо получить
     * @return значение поля
     * @throws RuntimeException если произошла ошибка доступа к полю
     */
    public static Object getFieldValue(Object obj, Field field) {
        try {
            field.setAccessible(true);
            return field.get(obj);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(field.getName(), ex);
        }
    }

}
