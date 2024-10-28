package com.example.project.mvc;

import com.example.project.mvc.models.CollegeStudent;
import com.example.project.mvc.repository.StudentDao;
import com.example.project.mvc.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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

@TestPropertySource("/application.properties")
@SpringBootTest
class StudentAndGradeServiceTest {

	@Autowired
	private StudentAndGradeService studentAndGradeService;

	@Autowired
	private StudentDao studentDao;

	@Autowired
	private JdbcTemplate jdbc;

	@BeforeEach
	public void setUpDatabase() {
		jdbc.execute("INSERT INTO student (id, firstname, lastname, email_address) "
			+ "values (1, 'Eric', 'Roby', 'eric.roby@luv2code.com')");
	}

	@Test
	public void createStudentService() {
		studentAndGradeService.createStudent("Chad", "Darby", "chad.darby@outlook.com");
		CollegeStudent student = studentDao.findByEmailAddress("chad.darby@outlook.com");
		Assertions.assertEquals("chad.darby@outlook.com", student.getEmailAddress(), "find by email");
	}

	@Test
	public void isStudentNullCheck() {
		Assertions.assertTrue(studentAndGradeService.checkIfStudentIsNull(1));
		Assertions.assertFalse(studentAndGradeService.checkIfStudentIsNull(2));
	}

	@Test
	public void deleteStudentService() {
		Optional<CollegeStudent> deletedStudent = studentDao.findById(1);
		Assertions.assertTrue(deletedStudent.isPresent(), "should return true");
		studentAndGradeService.deleteStudent(1);
		deletedStudent = studentDao.findById(1);
		Assertions.assertFalse(deletedStudent.isPresent(), "should return false");
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

		Assertions.assertEquals(5, collegeStudents.size());
	}

	@AfterEach
	public void setUpAfterTransaction() {
		jdbc.execute("DELETE FROM student");
	}
}
