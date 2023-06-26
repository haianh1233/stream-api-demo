package org.example.stream;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Student {
    private String name;
    private double avgGrade;

    @Override
    public String toString() {
        return "Student name: " + name + " - " + "avg grade: " + avgGrade;
    }
}
