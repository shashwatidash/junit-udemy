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
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource("/application-test.properties")
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
		CollegeStudent student = new CollegeStudent("Eric", "Roby", "eric.roby@udemy.com");
		student.setId(1);
		studentDao.save(student);

		MathGrade mathGrade = new MathGrade(100.00);
		mathGrade.setStudentId(1);
		mathGrade.setId(1);
		mathGradesDao.save(mathGrade);

		ScienceGrade scienceGrade = new ScienceGrade(100.00);
		scienceGrade.setStudentId(1);
		scienceGrade.setId(1);
		scienceGradesDao.save(scienceGrade);

		HistoryGrade historyGrade = new HistoryGrade(100.00);
		historyGrade.setStudentId(1);
		historyGrade.setId(1);
		historyGradesDao.save(historyGrade);
	}

	@Test
	public void createStudentService() {
		studentAndGradeService.createStudent("Chad", "Darby", "chad.darby@outlook.com");
		CollegeStudent student = studentDao.findByEmailAddress("chad.darby@outlook.com");
		assertEquals("chad.darby@outlook.com", student.getEmailAddress(), "find by email");
	}

	@Test
	public void isStudentNullCheck() {
		assertTrue(studentAndGradeService.checkIfStudentIsNull(2));
		assertFalse(studentAndGradeService.checkIfStudentIsNull(1));
	}

	@Test
	public void deleteStudentService() {
		Optional<CollegeStudent> deletedStudent = studentDao.findById(1);
		Optional<MathGrade> mathGrade = mathGradesDao.findById(1);
		Optional<ScienceGrade> scienceGrade = scienceGradesDao.findById(1);
		Optional<HistoryGrade> historyGrade = historyGradesDao.findById(1);

		assertTrue(deletedStudent.isPresent(), "should return true");
		assertTrue(mathGrade.isPresent(), "should return true");
		assertTrue(scienceGrade.isPresent(), "should return true");
		assertTrue(historyGrade.isPresent(), "should return true");

		studentAndGradeService.deleteStudent(1);
		deletedStudent = studentDao.findById(1);
		mathGrade = mathGradesDao.findById(1);
		scienceGrade = scienceGradesDao.findById(1);
		historyGrade = historyGradesDao.findById(1);

		assertFalse(deletedStudent.isPresent(), "should return false");
		assertFalse(mathGrade.isPresent(), "should return false");
		assertFalse(scienceGrade.isPresent(), "should return false");
		assertFalse(historyGrade.isPresent(), "should return false");
	}

	// execute sql before the method
	@SqlGroup({@Sql(scripts = "/insertData.sql", config = @SqlConfig(commentPrefix = "`")),
		@Sql("/overrideData.sql"),
		@Sql("/insertGrade.sql")})
	@Test
	public void getGradeBookService() {
		GradeBook gradeBook = studentAndGradeService.getGradeBook();
		GradeBook gradeBookTest = new GradeBook();
		for (GradeBookCollegeStudent student : gradeBook.getStudents()) {
			if (student.getId() > 10) {
				gradeBookTest.getStudents().add(student);
			}
		}
		assertEquals(4, gradeBookTest.getStudents().size());
		assertTrue(gradeBookTest.getStudents().get(0).getStudentGrades().getHistoryGradesResult() != null);
		assertTrue(gradeBookTest.getStudents().get(0).getStudentGrades().getScienceGradesResult() != null);
		assertTrue(gradeBookTest.getStudents().get(0).getStudentGrades().getMathGradesResult() != null);
	}

	@Test
	public void createGradeService() {
		// create grade
		assertTrue(studentAndGradeService.createGrade(99.75, 1, "math"));
		assertTrue(studentAndGradeService.createGrade(89.05, 1, "science"));
		assertTrue(studentAndGradeService.createGrade(87.00, 1, "history"));
		// get all grades with studentId
		Iterable<MathGrade> mathGrades = mathGradesDao.findGradesByStudentId(1);
		Iterable<ScienceGrade> scienceGrades = scienceGradesDao.findGradesByStudentId(1);
		Iterable<HistoryGrade> historyGrades = historyGradesDao.findGradesByStudentId(1);
		// verify there is grades
		assertTrue(mathGrades.iterator().hasNext(), "Student has math grade");
		assertTrue(scienceGrades.iterator().hasNext(), "Student has science grade");
		assertTrue(historyGrades.iterator().hasNext(), "Student has history grade");
	}

	@Test
	public void createGradeServiceReturnFalse() {
		assertFalse(studentAndGradeService.createGrade(105, 1, "math"));
		assertFalse(studentAndGradeService.createGrade(-5, 1, "math"));
		assertFalse(studentAndGradeService.createGrade(80, 3, "math"));
		assertFalse(studentAndGradeService.createGrade(100, 1, "english"));
	}
	
	@Test
	public void deleteGradeService() {
		assertEquals(1, studentAndGradeService.deleteGrade(1, "math"),
				"returns student id after delete");
		assertEquals(1, studentAndGradeService.deleteGrade(1, "science"),
				"returns student id after delete");
		assertEquals(1, studentAndGradeService.deleteGrade(1, "history"),
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
		GradeBookCollegeStudent gradeBookCollegeStudent = studentAndGradeService.studentInformation(1);
		assertNotNull(gradeBookCollegeStudent);
		assertEquals(1, gradeBookCollegeStudent.getId());
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
		jdbc.execute("DELETE FROM student");
		jdbc.execute("ALTER TABLE student ALTER COLUMN ID RESTART WITH 1");
		jdbc.execute(sqlDeleteMathGrade);
		jdbc.execute("ALTER TABLE math_grade ALTER COLUMN ID RESTART WITH 1");
		jdbc.execute(sqlDeleteScienceGrade);
		jdbc.execute("ALTER TABLE science_grade ALTER COLUMN ID RESTART WITH 1");
		jdbc.execute(sqlDeleteHistoryGrade);
		jdbc.execute("ALTER TABLE history_grade ALTER COLUMN ID RESTART WITH 1");
	}
}
