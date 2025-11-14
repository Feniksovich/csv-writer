package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.annotations.CsvProperty;

import java.time.Month;

@Data
@Builder
@AllArgsConstructor
public class Person {
    @CsvProperty(order = 2)
    private String firstName;

    @CsvProperty(order = 1)
    private String lastName;

    @CsvProperty(name = "day")
    private int dayOfBirth;

    @CsvProperty(name = "month")
    private Month monthOfBirth;

    @CsvProperty(name = "year")
    private int yearOfBirth;
}
