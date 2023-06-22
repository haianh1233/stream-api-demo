package org.example;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StreamApiDemo {

    private List<Student> initStudents() {
        List<Student> students = new ArrayList<>();

        students.add(new Student("Shara", 8.5));
        students.add(new Student("Cage", 6));
        students.add(new Student("Tom", 9.5));
        students.add(new Student("Tommy", 9.5));
        students.add(new Student("John", 7.5));
        students.add(new Student("Ling", 5.6));

        return students;
    }

    @Test
    public void statelessPipelinesExecution() {
        List<Student> students = initStudents();

        List<Student> studentsWithBonusGrade = students.stream()
                .filter(s -> {
                    System.out.println("Filter student: " + s.getName());
                    return s.getAvgGrade() > 7;
                })
                .map(s -> {
                    System.out.println("Add 0.5 bonus for student has avg grade > 7");
                    return new Student(s.getName(), s.getAvgGrade() + 0.5);
                })
                .collect(Collectors.toList());

        studentsWithBonusGrade.forEach(t -> System.out.println(t.toString()));
    }

    @Test
    public void stateFullPipelinesExecution() {
        List<Student> students = initStudents();

        List<Student> studentsWithBonusGrade = students.stream()
                .filter(s -> {
                    System.out.println("Filter student: " + s.getName());
                    return s.getAvgGrade() > 7;
                })
                .sorted(Comparator.comparingDouble(Student::getAvgGrade))
                .map(s -> {
                    System.out.println("Add 0.5 bonus for student has avg grade > 7");
                    return new Student(s.getName(), s.getAvgGrade() + 0.5);
                })
                .collect(Collectors.toList());

        // The sorted method cannot emit a result until all the elements have been filtered, so it buffers them before
        // emitting any result to the next stage

        studentsWithBonusGrade.forEach(t -> System.out.println(t.toString()));
    }

    @Test
    public void parallelStreamExecution() {
        List<Student> students = initStudents();

        List<Student> studentsWithBonusGrade = students.stream()
                .parallel()
                .filter(s -> {
                    System.out.println(Thread.currentThread() + "-Filter student: " + s.getName());
                    return s.getAvgGrade() > 7;
                })
                .sorted(Comparator.comparingDouble(Student::getAvgGrade))
                .map(s -> {
                    System.out.println(Thread.currentThread() + "-Add 0.5 bonus for student has avg grade > 7");
                    return new Student(s.getName(), s.getAvgGrade() + 0.5);
                })
                .collect(Collectors.toList());

    }
}