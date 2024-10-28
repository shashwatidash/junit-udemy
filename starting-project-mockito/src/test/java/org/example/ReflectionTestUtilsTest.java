package org.example;

import org.example.models.CollegeStudent;
import org.example.models.StudentGrades;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = App.class)
public class ReflectionTestUtilsTest {
    @Autowired
    ApplicationContext context;

    @Autowired
    CollegeStudent student;

    @Autowired
    StudentGrades studentGrades;

    @BeforeEach
    public void init() {
        student.setFirstname("Henry");
        student.setLastname("Clark");
        student.setEmailAddress("hclark@hotmail.com");
        student.setStudentGrades(studentGrades);

        ReflectionTestUtils.setField(student, "id", 101);
        ReflectionTestUtils.setField(student, "studentGrades", new StudentGrades(
                new ArrayList<>(Arrays.asList(100.0, 85.0, 76.50, 91.75))
        ));
    }

    @Test
    public void getPrivateField() {
        assertEquals(101, ReflectionTestUtils.getField(student, "id"));
    }

    @Test
    public void invokePrivateMethod() {
        assertEquals("Henry 101",
                ReflectionTestUtils.invokeMethod(student, "getFirstNameAndId"));
    }

}
