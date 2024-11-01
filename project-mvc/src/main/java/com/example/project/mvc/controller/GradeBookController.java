package com.example.project.mvc.controller;

import com.example.project.mvc.models.CollegeStudent;
import com.example.project.mvc.models.GradeBook;
import com.example.project.mvc.models.GradeBookCollegeStudent;
import com.example.project.mvc.service.StudentAndGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class GradeBookController {
    @Autowired
    private GradeBook gradeBook;

    @Autowired
    private StudentAndGradeService studentAndGradeService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getStudents(Model model) {
        Iterable<CollegeStudent> collegeStudents = studentAndGradeService.getGradeBook();
        model.addAttribute("students", collegeStudents);
        return "index";
    }

    @PostMapping(value = "/")
    public String createStudent(@ModelAttribute("student") CollegeStudent student, Model model) {
        studentAndGradeService.createStudent(student.getFirstname(),
                student.getLastname(), student.getEmailAddress());
        Iterable<CollegeStudent> collegeStudentList = studentAndGradeService.getGradeBook();
        model.addAttribute("students", collegeStudentList);
        return "index";
    }

    @GetMapping(value = "/delete/student/{id}")
    public String deleteStudent(@PathVariable int id, Model model) {
        if (studentAndGradeService.checkIfStudentIsNull(id)) {
            return "error";
        }
        studentAndGradeService.deleteStudent(id);
        Iterable<CollegeStudent> collegeStudents = studentAndGradeService.getGradeBook();
        model.addAttribute("students", collegeStudents);
        return "index";
    }

    @GetMapping("/studentInformation/{id}")
    public String studentInformation(@PathVariable int id, Model model) {
        if (studentAndGradeService.checkIfStudentIsNull(id)) {
            return "error";
        }
        GradeBookCollegeStudent student = studentAndGradeService.studentInformation(id);
        model.addAttribute("student", student);
        if (!student.getStudentGrades().getMathGradesResult().isEmpty()) {
            model.addAttribute("mathAverage", student.getStudentGrades()
                    .findGradePointAverage(student.getStudentGrades().getMathGradesResult()));
        } else {
            model.addAttribute("mathAverage", "N/A");
        }

        if (!student.getStudentGrades().getScienceGradesResult().isEmpty()) {
            model.addAttribute("scienceAverage", student.getStudentGrades()
                    .findGradePointAverage(student.getStudentGrades().getScienceGradesResult()));
        } else {
            model.addAttribute("scienceAverage", "N/A");
        }

        if (!student.getStudentGrades().getHistoryGradesResult().isEmpty()) {
            model.addAttribute("historyAverage", student.getStudentGrades()
                    .findGradePointAverage(student.getStudentGrades().getHistoryGradesResult()));
        } else {
            model.addAttribute("historyAverage", "N/A");
        }
        return "studentInformation";
    }
}
