package org.writer;

import org.writer.annotations.CsvProperty;
import org.writer.utils.ReflectionUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

public class CsvWriter implements Writable {

    private static final String DELIMITER = ",";

    @Override
    public void writeToFile(List<?> data, String fileName) throws IOException {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("data is null or empty");
        }

        final Class<?> clazz = data.get(0).getClass();
        final List<PropertyMetadata> metadata = getOrderedProperties(clazz);

        if (metadata.isEmpty()) {
            throw new IllegalArgumentException("no annotated fields found in class %s".formatted(clazz.getName()));
        }

        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            final String header = String.join(DELIMITER, getColumns(metadata));
            writer.write(header);
            writer.newLine();

            for (final Object object : data) {
                final List<String> values = getValues(object, metadata);
                final String line = String.join(DELIMITER, values);
                writer.write(line);
                writer.newLine();
            }
        }
    }

    private List<String> getColumns(List<PropertyMetadata> metadata) {
        return metadata.stream()
                .map(PropertyMetadata::name)
                .toList();
    }

    private List<String> getValues(Object obj, List<PropertyMetadata> metadata) {
        return metadata.stream()
                .map(property -> ReflectionUtils.getFieldValue(obj, property.field))
                .map(String::valueOf)
                .toList();
    }

    private List<PropertyMetadata> getOrderedProperties(Class<?> clazz) {
        final List<Field> fields = ReflectionUtils.getAnnotatedFields(CsvProperty.class, clazz);
        final List<PropertyMetadata> properties = new ArrayList<>();

        for (final Field field : fields) {
            final CsvProperty annotation = field.getAnnotation(CsvProperty.class);
            final String name = annotation.name().isBlank() ? field.getName() : annotation.name();
            final int order = annotation.order();

            if (order < 0) {
                throw new IllegalArgumentException("illegal negative order %d for field '%s' in class '%s'"
                        .formatted(order, field.getName(), clazz.getName()));
            }

            properties.add(new PropertyMetadata(field, name, annotation.order()));
        }

        properties.sort(Comparator.comparingInt(PropertyMetadata::order));
        return properties;
    }

    private record PropertyMetadata(Field field, String name, int order) {}
}
