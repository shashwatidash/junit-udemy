package com.example.project;

import com.example.project.models.CollegeStudent;
import com.example.project.models.StudentGrades;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MvcTestingApplicationTest {

	private static int count = 0;

	@Value("${info.app.name}")
	private String appInfo;

	@Value("${info.app.description}")
	private String appDescription;

	@Value("${info.app.version}")
	private String appVersion;

	@Autowired
	ApplicationContext context;

	@Autowired
	StudentGrades studentGrades;

	@Autowired
	CollegeStudent collegeStudent;

	@BeforeEach
	public void beforeEach() {
		count = count + 1;
		System.out.println("Testing: " + appInfo + " which is a " + appDescription +
				" Version: " + appVersion + ". Execution of test method " + count);
		collegeStudent.setFirstName("Yashika");
		collegeStudent.setLastName("Verma");
		collegeStudent.setEmailAddress("yashikav@gmail.com");
		studentGrades.setMathGradeResults(new ArrayList<>(Arrays.asList(100.0, 85.0, 76.50, 91.75)));
		collegeStudent.setStudentGrades(studentGrades);
	}

	@Test
	@DisplayName("Add grade results for student grades")
	public void addGradeResults() {
		assertEquals(353.25,
				studentGrades.addGradeResultsForSingleClass(collegeStudent.getStudentGrades().getMathGradeResults()));
	}

	@Test
	@DisplayName("Add grade results for student grades not equal")
	public void addGradeResultsNotEqual() {
		assertNotEquals(0,
				studentGrades.addGradeResultsForSingleClass(collegeStudent.getStudentGrades().getMathGradeResults()));
	}

	@Test
	@DisplayName("Is grade greater true")
	public void checkGreater() {
		assertTrue(studentGrades.isGradeGreater(90, 75), "test should pass");
	}

	@Test
	@DisplayName("Is grade greater false")
	public void checkNotGreater() {
		assertFalse(studentGrades.isGradeGreater(75, 90), "test should return false");
	}

	@Test
	@DisplayName("Check Null for grades")
	public void checkForNullGrades() {
		assertNotNull(studentGrades.checkNull(collegeStudent.getStudentGrades().getMathGradeResults()),
				"object should not be null");
	}

	@Test
	@DisplayName("Create student without grade init")
	public void createStudentWithoutGrades() {
		CollegeStudent collegeStudentTwo = context.getBean("collegeStudent", CollegeStudent.class);
		collegeStudentTwo.setFirstName("Chad");
		collegeStudentTwo.setLastName("Darby");
		collegeStudentTwo.setEmailAddress("darbychad@hotmail.com");
		assertNotNull(collegeStudentTwo.getFirstName());
		assertNotNull(collegeStudentTwo.getLastName());
		assertNotNull(collegeStudentTwo.getEmailAddress());
		assertNull(studentGrades.checkNull(collegeStudentTwo.getStudentGrades()));
	}

	@Test
	@DisplayName("Verify students are prototypes")
	public void verifyStudentsArePrototypes() {
		CollegeStudent collegeStudentTwo = context.getBean("collegeStudent", CollegeStudent.class);
		assertNotSame(collegeStudent, collegeStudentTwo);
	}

	@Test
	@DisplayName("Test Grade Point Average")
	public void testGradePointAverage() {
		assertAll("Testing all assertEquals",
				() -> assertEquals(353.25, studentGrades.addGradeResultsForSingleClass(
						collegeStudent.getStudentGrades().getMathGradeResults())),
				() -> assertEquals(88.31, studentGrades.findGradePointAverage(
						collegeStudent.getStudentGrades().getMathGradeResults()))
				);
	}

	@Test
	void contextLoads() {
	}

}
