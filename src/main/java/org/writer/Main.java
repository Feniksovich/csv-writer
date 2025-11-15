package org.writer;

import org.writer.model.Person;
import org.writer.model.Student;

import java.io.IOException;
import java.time.Month;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        final List<Person> persons = List.of(
                new Person("John", "Doe", 30, Month.APRIL, 1995),
                new Person("Jane", "Smith", 25, Month.DECEMBER, 1998),
                new Person("Alice", "Johnson", 28, Month.JUNE, 1995)
        );

        final List<Student> students = List.of(
                new Student("Tom", List.of("9.5", "8.0", "7.5")),
                new Student("Lucy", List.of("8.5", "9.0", "9.5")),
                new Student("Mark", List.of("7.0", "8.0", "8.5"))
        );

        final CsvWriter writer = new CsvWriter();
        try {
            writer.writeToFile(persons, "persons.csv");
            writer.writeToFile(students, "students.csv");
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
}