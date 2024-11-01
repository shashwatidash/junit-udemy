package com.example.project.mvc.controller;

import com.example.project.mvc.exceptionhanders.StudentOrGradeErrorResponse;
import com.example.project.mvc.exceptionhanders.StudentOrGradeNotFoundException;
import com.example.project.mvc.models.CollegeStudent;
import com.example.project.mvc.models.GradeBook;
import com.example.project.mvc.models.GradeBookCollegeStudent;
import com.example.project.mvc.service.StudentAndGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GradeBookController {

    @Autowired
    private GradeBook gradeBook;

    @Autowired
    private StudentAndGradeService studentAndGradeService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<GradeBookCollegeStudent> getStudents(Model model) {
        gradeBook = studentAndGradeService.getGradeBook();
        return gradeBook.getStudents();
    }

    @PostMapping(value = "/")
    public List<GradeBookCollegeStudent> createStudent(@RequestBody CollegeStudent student) {
        studentAndGradeService.createStudent(student.getFirstname(),
                student.getLastname(), student.getEmailAddress());
        gradeBook = studentAndGradeService.getGradeBook();
        return gradeBook.getStudents();
    }

    @DeleteMapping(value = "/student/{id}")
    public List<GradeBookCollegeStudent> deleteStudent(@PathVariable int id, Model model) {
        if (studentAndGradeService.checkIfStudentIsNull(id)) {
            throw new StudentOrGradeNotFoundException("Student Or Grade was not found -");
        }
        studentAndGradeService.deleteStudent(id);
        gradeBook = studentAndGradeService.getGradeBook();
        return gradeBook.getStudents();
    }

    @GetMapping("/studentInformation/{id}")
    public GradeBookCollegeStudent studentInformation(@PathVariable int id) {
        if (studentAndGradeService.checkIfStudentIsNull(id)) {
            throw new StudentOrGradeNotFoundException("Student Or Grade was not found -");
        }
        GradeBookCollegeStudent student = studentAndGradeService.studentInformation(id);
        return student;
    }

    @PostMapping("/grades")
    public GradeBookCollegeStudent createGrade(@RequestParam("grade") double grade, @RequestParam("gradeType") String gradeType,
                              @RequestParam("studentId") int studentId) {
        if (studentAndGradeService.checkIfStudentIsNull(studentId)) {
            throw new StudentOrGradeNotFoundException("Student Or Grade was not found -");
        }
        boolean success = studentAndGradeService.createGrade(grade, studentId, gradeType);
        if (!success) {
            throw new StudentOrGradeNotFoundException("Student Or Grade was not found -");
        }

        GradeBookCollegeStudent collegeStudent = studentAndGradeService.studentInformation(studentId);
        if (collegeStudent == null) {
            throw new StudentOrGradeNotFoundException("Student Or Grade was not found -");
        }
        return collegeStudent;
    }

    @DeleteMapping("/grades/{id}/{gradeType}")
    public GradeBookCollegeStudent deleteGrade(@PathVariable int id, @PathVariable String gradeType, Model model) {
        int studentId = studentAndGradeService.deleteGrade(id, gradeType);
        if (studentId == 0) {
            throw new StudentOrGradeNotFoundException("Student Or Grade was not found -");
        }
        GradeBookCollegeStudent collegeStudent = studentAndGradeService.studentInformation(studentId);
        return collegeStudent;
    }

    @ExceptionHandler
    public ResponseEntity<StudentOrGradeErrorResponse> handleException(StudentOrGradeNotFoundException exc) {
        StudentOrGradeErrorResponse error = new StudentOrGradeErrorResponse();
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
