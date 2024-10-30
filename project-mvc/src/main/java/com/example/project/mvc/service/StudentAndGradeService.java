package com.example.project.mvc.service;

import com.example.project.mvc.models.*;
import com.example.project.mvc.repository.HistoryGradesDao;
import com.example.project.mvc.repository.MathGradesDao;
import com.example.project.mvc.repository.ScienceGradesDao;
import com.example.project.mvc.repository.StudentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentAndGradeService {

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private MathGradesDao mathGradesDao;

    @Autowired
    private ScienceGradesDao scienceGradesDao;

    @Autowired
    private HistoryGradesDao historyGradesDao;

    @Autowired
    @Qualifier("mathGrades")
    private MathGrade mathGrade;

    @Autowired
    @Qualifier("scienceGrades")
    private ScienceGrade scienceGrade;

    @Autowired
    @Qualifier("historyGrades")
    private HistoryGrade historyGrade;

    @Autowired
    private StudentGrades studentGrades;

    public void createStudent(String firstname, String lastname, String email) {
        CollegeStudent student = new CollegeStudent(firstname, lastname, email);
        student.setId(0);
        studentDao.save(student);
    }

    public boolean checkIfStudentIsNull(int id) {
        Optional<CollegeStudent> student = studentDao.findById(id);
        return student.isEmpty();
    }

    public void deleteStudent(int id) {
        if (!checkIfStudentIsNull(id)) {
            studentDao.deleteById(id);

            mathGradesDao.deleteByStudentId(id);
            scienceGradesDao.deleteByStudentId(id);
            historyGradesDao.deleteByStudentId(id);
        }
    }

    public Iterable<CollegeStudent> getGradeBook() {
        return studentDao.findAll();
    }

    public boolean createGrade(double grade, int id, String gradeType) {
        if (checkIfStudentIsNull(id)) {
            return false;
        }

        if (grade >= 0 && grade <= 100) {
            if (gradeType.equals("math")) {
                mathGrade.setId(0);
                mathGrade.setGrade(grade);
                mathGrade.setStudentId(id);
                mathGradesDao.save(mathGrade);
                return true;
            } else if (gradeType.equals("science")) {
                scienceGrade.setId(0);
                scienceGrade.setGrade(grade);
                scienceGrade.setStudentId(id);
                scienceGradesDao.save(scienceGrade);
                return true;
            } else if (gradeType.equals("history")) {
                historyGrade.setId(0);
                historyGrade.setGrade(grade);
                historyGrade.setStudentId(id);
                historyGradesDao.save(historyGrade);
                return true;
            }
        }
        return false;
    }

    public int deleteGrade(int id, String gradeType) {
        int studentId = 0;
        if (gradeType.equals("math")) {
            Optional<MathGrade> grade = mathGradesDao.findById(id);
            if (grade.isEmpty()) {
                return studentId;
            }
            studentId = grade.get().getStudentId();
            mathGradesDao.deleteById(studentId);
        } else if (gradeType.equals("science")) {
            Optional<ScienceGrade> grade = scienceGradesDao.findById(id);
            if (grade.isEmpty()) {
                return studentId;
            }
            studentId = grade.get().getStudentId();
            scienceGradesDao.deleteById(studentId);
        } else if (gradeType.equals("history")) {
            Optional<HistoryGrade> grade = historyGradesDao.findById(id);
            if (grade.isEmpty()) {
                return studentId;
            }
            studentId = grade.get().getStudentId();
            historyGradesDao.deleteById(studentId);
        }
        return studentId;
    }

    public GradeBookCollegeStudent studentInformation(int id) {
        if (checkIfStudentIsNull(id)) {
            return null;
        }
        Optional<CollegeStudent> collegeStudent = studentDao.findById(id);
        Iterable<MathGrade> mathGrades = mathGradesDao.findGradesByStudentId(id);
        Iterable<ScienceGrade> scienceGrades = scienceGradesDao.findGradesByStudentId(id);
        Iterable<HistoryGrade> historyGrades = historyGradesDao.findGradesByStudentId(id);

        List<Grade> mathGradesList = new ArrayList<>();
        mathGrades.forEach(mathGradesList::add);

        List<Grade> scienceGradesList = new ArrayList<>();
        scienceGrades.forEach(scienceGradesList::add);

        List<Grade> historyGradesList = new ArrayList<>();
        historyGrades.forEach(historyGradesList::add);

        studentGrades.setMathGradesResult(mathGradesList);
        studentGrades.setScienceGradesResult(scienceGradesList);
        studentGrades.setHistoryGradesResult(historyGradesList);

        GradeBookCollegeStudent gradeBookCollegeStudent = new
                GradeBookCollegeStudent(collegeStudent.get().getId(), collegeStudent.get().getFirstname(),
                collegeStudent.get().getLastname(), collegeStudent.get().getEmailAddress(), studentGrades);
        return gradeBookCollegeStudent;
    }
}
