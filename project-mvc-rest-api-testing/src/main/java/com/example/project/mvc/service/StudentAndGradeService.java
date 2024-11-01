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
import org.springframework.ui.Model;

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
//        student.setId(0);
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

    public GradeBook getGradeBook() {
        Iterable<CollegeStudent> collegeStudents = studentDao.findAll();
        Iterable<MathGrade> mathGrades = mathGradesDao.findAll();
        Iterable<ScienceGrade> scienceGrades = scienceGradesDao.findAll();
        Iterable<HistoryGrade> historyGrades = historyGradesDao.findAll();

        GradeBook gradebook = new GradeBook();
        for (CollegeStudent collegeStudent : collegeStudents) {
            List<Grade> mathGradesPerStudent = new ArrayList<>();
            List<Grade> scienceGradesPerStudent = new ArrayList<>();
            List<Grade> historyGradesPerStudent = new ArrayList<>();

            for (MathGrade grade : mathGrades) {
                if (grade.getStudentId() == collegeStudent.getId()) {
                    mathGradesPerStudent.add(grade);
                }
            }
            for (ScienceGrade grade : scienceGrades) {
                if (grade.getStudentId() == collegeStudent.getId()) {
                    scienceGradesPerStudent.add(grade);
                }
            }

            for (HistoryGrade grade : historyGrades) {
                if (grade.getStudentId() == collegeStudent.getId()) {
                    historyGradesPerStudent.add(grade);
                }
            }

            studentGrades.setMathGradesResult(mathGradesPerStudent);
            studentGrades.setScienceGradesResult(scienceGradesPerStudent);
            studentGrades.setHistoryGradesResult(historyGradesPerStudent);

            GradeBookCollegeStudent gradebookCollegeStudent = new GradeBookCollegeStudent(collegeStudent.getId(), collegeStudent.getFirstname(), collegeStudent.getLastname(),
                    collegeStudent.getEmailAddress(), studentGrades);

            gradebook.getStudents().add(gradebookCollegeStudent);
        }

        return gradebook;
    }

    public void configureStudentInformationModel(int id, Model model) {
        GradeBookCollegeStudent student = studentInformation(id);
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
    }
}
