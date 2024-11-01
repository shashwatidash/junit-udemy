package com.example.project.mvc.models;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GradeBook {
    private List<GradeBookCollegeStudent> students = new ArrayList<>();

    public GradeBook() {}

    public GradeBook(List<GradeBookCollegeStudent> students) {
        this.students = students;
    }

    public List<GradeBookCollegeStudent> getStudents() {
        return students;
    }

    public void setStudents(List<GradeBookCollegeStudent> students) {
        this.students = students;
    }
}
