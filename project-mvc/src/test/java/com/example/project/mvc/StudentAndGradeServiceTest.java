package com.example.project.mvc;

import com.example.project.mvc.models.CollegeStudent;
import com.example.project.mvc.models.HistoryGrade;
import com.example.project.mvc.models.MathGrade;
import com.example.project.mvc.models.ScienceGrade;
import com.example.project.mvc.repository.HistoryGradesDao;
import com.example.project.mvc.repository.MathGradesDao;
import com.example.project.mvc.repository.ScienceGradesDao;
import com.example.project.mvc.repository.StudentDao;
import com.example.project.mvc.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

	@BeforeEach
	public void setUpDatabase() {
		jdbc.execute("INSERT INTO student (id, firstname, lastname, email_address) "
			+ "values (2, 'Eric', 'Roby', 'eric.roby@udemy.com')");
		jdbc.execute("INSERT INTO math_grade (id, student_id, grade) values (2, 2, 100.00)");
		jdbc.execute("INSERT INTO science_grade (id, student_id, grade) values (2, 2, 100.00)");
		jdbc.execute("INSERT INTO history_grade (id, student_id, grade) values (2, 2, 100.00)");
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

	@AfterEach
	public void setUpAfterTransaction() {
		jdbc.execute("DELETE FROM student");
		jdbc.execute("DELETE FROM math_grade");
		jdbc.execute("DELETE FROM science_grade");
		jdbc.execute("DELETE FROM history_grade");
	}
}
