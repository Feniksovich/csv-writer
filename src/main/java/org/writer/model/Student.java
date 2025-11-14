package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.annotations.CsvProperty;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Student {
    @CsvProperty
    private String name;

    @CsvProperty
    private List<String> score;
}