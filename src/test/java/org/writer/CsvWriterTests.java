package org.writer;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.writer.annotations.CsvProperty;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class CsvWriterTests {

    @TempDir
    private Path tempDir;

    private final CsvWriter csvWriter = new CsvWriter();
    private final Supplier<Path> targetPath = () -> tempDir.resolve("test.csv");

    @Test
    void nullData_shouldThrowException() {
        final Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> csvWriter.writeToFile(null, targetPath.get())
        );
        assertEquals("data is null or empty", ex.getMessage());
    }

    @Test
    void emptyData_shouldThrowException() {
        final Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> csvWriter.writeToFile(Collections.emptyList(), targetPath.get())
        );
        assertEquals("data is null or empty", ex.getMessage());
    }

    @Test
    void fieldHasNegativeOrder_shouldThrowException() {
        class InvalidOrderClass {
            @CsvProperty(order = -1)
            private String field;
        }

        final List<InvalidOrderClass> data = List.of(new InvalidOrderClass());
        final Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> csvWriter.writeToFile(data, targetPath.get())
        );

        assertTrue(ex.getMessage().contains("illegal negative order"));
    }

    @Test
    void classHasNoAnnotatedFields_shouldThrowException() {
        class EmptyClass {
            private String field;
        }

        final List<EmptyClass> data = List.of(new EmptyClass());
        final Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> csvWriter.writeToFile(data, targetPath.get())
        );

        assertTrue(ex.getMessage().contains("no annotated fields found in class"));
    }

    @Test
    void classHasOrderedFields_thenFieldsAreSortedByOrder() throws IOException {
        class OrderedClass {
            @CsvProperty
            private String field1;

            @CsvProperty(order = 2)
            private String field2;

            @CsvProperty(order = 1)
            private String field3;
        }

        final List<OrderedClass> data = List.of(new OrderedClass());
        final Path target = targetPath.get();

        csvWriter.writeToFile(data, target);
        assertTrue(Files.exists(target));

        final String header = Files.readAllLines(target).get(0);
        assertEquals("field3,field2,field1", header);
    }

    @Test
    void classHasNamedFields_thenCustomNamesUsed() throws IOException {
        class NamedClass {
            @CsvProperty(name = "first_name")
            private String firstName;

            @CsvProperty(name = "last_name")
            private String lastName;
        }

        final List<NamedClass> data = List.of(new NamedClass());
        final Path target = targetPath.get();

        csvWriter.writeToFile(data, target);
        assertTrue(Files.exists(target));

        final String header = Files.readAllLines(target).get(0);
        assertTrue(header.contains("first_name"));
        assertTrue(header.contains("last_name"));
    }

    @Test
    void classHasNoNamedFields_thenClassFieldNameUsed() throws IOException {
        class NotNamedClass {
            @CsvProperty
            private String firstName;

            @CsvProperty
            private String lastName;
        }

        final List<NotNamedClass> data = List.of(new NotNamedClass());
        final Path target = targetPath.get();

        csvWriter.writeToFile(data, target);
        assertTrue(Files.exists(target));

        final String header = Files.readAllLines(target).get(0);
        assertTrue(header.contains("firstName"));
        assertTrue(header.contains("lastName"));
    }

    @Test
    void validData_thenFileCreatedWithCorrectContent() throws IOException {

        @AllArgsConstructor
        class TestClass {
            @CsvProperty
            private String stringField;

            @CsvProperty
            private int intField;

            @CsvProperty
            private Month enumField;

            @CsvProperty
            private final Object nullField = null;
        }

        final List<TestClass> data = List.of(
                new TestClass("A", 1, Month.JANUARY),
                new TestClass("B", 2, Month.FEBRUARY),
                new TestClass("C", 3, Month.MARCH)
        );
        final Path target = targetPath.get();

        csvWriter.writeToFile(data, target);
        assertTrue(Files.exists(target));

        final List<String> lines = Files.readAllLines(target);
        assertEquals("stringField,intField,enumField,nullField", lines.get(0));
        assertEquals("A,1,JANUARY,null", lines.get(1));
        assertEquals("B,2,FEBRUARY,null", lines.get(2));
        assertEquals("C,3,MARCH,null", lines.get(3));
    }
}

