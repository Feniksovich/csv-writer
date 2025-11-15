package org.writer.annotations;

import java.lang.annotation.*;

/**
 * Аннотация для пометки полей класса, которые должны быть записаны в CSV файл.
 * Поля с этой аннотацией будут включены в CSV файл при использовании {@link org.writer.CsvWriter}.
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CsvProperty {
    /**
     * Название столбца в CSV представлении.
     * Используется имя поля класса по умолчанию.
     *
     * @return название столбца в CSV представлении
     */
    String name() default "";

    /**
     * Порядок сортировки столбца в CSV представлении.
     * Поля с order > 0 сортируются по возрастанию и располагаются перед полями с order = 0.
     * Значение должно быть неотрицательным.
     *
     * @return порядок сортировки столбца
     */
    int order() default 0;
}
