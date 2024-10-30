package com.example.project.mvc;

import com.example.project.mvc.models.*;
import com.example.project.mvc.repository.HistoryGradesDao;
import com.example.project.mvc.repository.MathGradesDao;
import com.example.project.mvc.repository.ScienceGradesDao;
import com.example.project.mvc.repository.StudentDao;
import com.example.project.mvc.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource("/application.properties")
@SpringBootTest
class StudentAndGradeServiceTest {

	@Autowired
	private StudentAndGradeService studentAndGradeService;

	@Autowired
	private StudentDao studentDao;

	@Autowired
	private MathGradesDao mathGradesDao;

	@Autowired
	private ScienceGradesDao scienceGradesDao;

	@Autowired
	private HistoryGradesDao historyGradesDao;

	@Autowired
	private JdbcTemplate jdbc;

	@Value("${sql.script.create.student}")
	private String sqlAddStudent;

	@Value("${sql.script.delete.student}")
	private String sqlDeleteStudent;

	@Value("${sql.script.create.math.grade}")
	private String sqlAddMathGrade;

	@Value("${sql.script.delete.math.grade}")
	private String sqlDeleteMathGrade;

	@Value("${sql.script.create.science.grade}")
	private String sqlAddScienceGrade;

	@Value("${sql.script.delete.science.grade}")
	private String sqlDeleteScienceGrade;

	@Value("${sql.script.create.history.grade}")
	private String sqlAddHistoryGrade;

	@Value("${sql.script.delete.history.grade}")
	private String sqlDeleteHistoryGrade;

	@BeforeEach
	public void setUpDatabase() {
		jdbc.execute(sqlAddStudent);
		jdbc.execute(sqlAddMathGrade);
		jdbc.execute(sqlAddScienceGrade);
		jdbc.execute(sqlAddHistoryGrade);
	}

	@Test
	public void createStudentService() {
		studentAndGradeService.createStudent("Chad", "Darby", "chad.darby@outlook.com");
		CollegeStudent student = studentDao.findByEmailAddress("chad.darby@outlook.com");
		assertEquals("chad.darby@outlook.com", student.getEmailAddress(), "find by email");
	}

	@Test
	public void isStudentNullCheck() {
		assertTrue(studentAndGradeService.checkIfStudentIsNull(1));
		assertFalse(studentAndGradeService.checkIfStudentIsNull(2));
	}

	@Test
	public void deleteStudentService() {
		Optional<CollegeStudent> deletedStudent = studentDao.findById(2);
		Optional<MathGrade> mathGrade = mathGradesDao.findById(2);
		Optional<ScienceGrade> scienceGrade = scienceGradesDao.findById(2);
		Optional<HistoryGrade> historyGrade = historyGradesDao.findById(2);

		assertTrue(deletedStudent.isPresent(), "should return true");
		assertTrue(mathGrade.isPresent(), "should return true");
		assertTrue(scienceGrade.isPresent(), "should return true");
		assertTrue(historyGrade.isPresent(), "should return true");

		studentAndGradeService.deleteStudent(2);
		deletedStudent = studentDao.findById(2);
		mathGrade = mathGradesDao.findById(2);
		scienceGrade = scienceGradesDao.findById(2);
		historyGrade = historyGradesDao.findById(2);

		assertFalse(deletedStudent.isPresent(), "should return false");
		assertFalse(mathGrade.isPresent(), "should return false");
		assertFalse(scienceGrade.isPresent(), "should return false");
		assertFalse(historyGrade.isPresent(), "should return false");
	}

	// execute sql before the method
	@Sql("/insertData.sql")
	@Test
	public void getGradeBookService() {
		Iterable<CollegeStudent> iterableCollegeStudent = studentAndGradeService.getGradeBook();
		List<CollegeStudent> collegeStudents = new ArrayList<>();
		for (CollegeStudent collegeStudent : iterableCollegeStudent) {
			collegeStudents.add(collegeStudent);
		}

		assertEquals(5, collegeStudents.size());
	}

	@Test
	public void createGradeService() {
		// create grade
		assertTrue(studentAndGradeService.createGrade(99.75, 2, "math"));
		assertTrue(studentAndGradeService.createGrade(89.05, 2, "science"));
		assertTrue(studentAndGradeService.createGrade(87.00, 2, "history"));
		// get all grades with studentId
		Iterable<MathGrade> mathGrades = mathGradesDao.findGradesByStudentId(2);
		Iterable<ScienceGrade> scienceGrades = scienceGradesDao.findGradesByStudentId(2);
		Iterable<HistoryGrade> historyGrades = historyGradesDao.findGradesByStudentId(2);
		// verify there is grades
		assertTrue(mathGrades.iterator().hasNext(), "Student has math grade");
		assertTrue(scienceGrades.iterator().hasNext(), "Student has science grade");
		assertTrue(historyGrades.iterator().hasNext(), "Student has history grade");
	}

	@Test
	public void createGradeServiceReturnFalse() {
		assertFalse(studentAndGradeService.createGrade(105, 2, "math"));
		assertFalse(studentAndGradeService.createGrade(-5, 2, "math"));
		assertFalse(studentAndGradeService.createGrade(80, 3, "math"));
		assertFalse(studentAndGradeService.createGrade(100, 2, "english"));
	}
	
	@Test
	public void deleteGradeService() {
		assertEquals(2, studentAndGradeService.deleteGrade(2, "math"),
				"returns student id after delete");
		assertEquals(2, studentAndGradeService.deleteGrade(2, "science"),
				"returns student id after delete");
		assertEquals(2, studentAndGradeService.deleteGrade(2, "history"),
				"returns student id after delete");
	}

	@Test
	public void deleteGradeServiceReturnsStudentIdOfZero() {
		assertEquals(0, studentAndGradeService.deleteGrade(0, "science"),
				"No student should have gradeId zero");
		assertEquals(0, studentAndGradeService.deleteGrade(0, "literature"),
				"No student should have literature class");
	}

	@Test
	public void studentInformation() {
		GradeBookCollegeStudent gradeBookCollegeStudent = studentAndGradeService.studentInformation(2);
		assertNotNull(gradeBookCollegeStudent);
		assertEquals(2, gradeBookCollegeStudent.getId());
		assertEquals("Eric", gradeBookCollegeStudent.getFirstname());
		assertEquals("Roby", gradeBookCollegeStudent.getLastname());
		assertEquals("eric.roby@udemy.com", gradeBookCollegeStudent.getEmailAddress());

		assertEquals(1, gradeBookCollegeStudent.getStudentGrades().getMathGradesResult().size());
        assertEquals(1, gradeBookCollegeStudent.getStudentGrades().getScienceGradesResult().size());
        assertEquals(1, gradeBookCollegeStudent.getStudentGrades().getHistoryGradesResult().size());
	}

	@Test
	public void studentInformationServiceReturnNull() {
		GradeBookCollegeStudent gradeBookCollegeStudent = studentAndGradeService.studentInformation(0);
		assertNull(gradeBookCollegeStudent);
	}

	@AfterEach
	public void setUpAfterTransaction() {
		jdbc.execute(sqlDeleteStudent);
		jdbc.execute(sqlDeleteMathGrade);
		jdbc.execute(sqlDeleteScienceGrade);
		jdbc.execute(sqlDeleteHistoryGrade);
	}
}
