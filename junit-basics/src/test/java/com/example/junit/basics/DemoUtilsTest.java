package com.example.junit.basics;

import org.junit.jupiter.api.*;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.MethodName.class)
//@DisplayNameGeneration(DisplayNameGenerator.IndicativeSentences.class)
public class DemoUtilsTest {

    DemoUtils demoUtils;

    @BeforeAll
    static void setUpBeforeAll() {
        System.out.println("@BeforeAll executes once before all the test methods");
    }

    @BeforeEach
    void setUpBeforeEach() {
        demoUtils = new DemoUtils();
        System.out.println("@BeforeEach executes before the execution of each method");
    }

    @Test
     @DisplayName("Equality Check")
    void testEqualsAndNotEquals() {
        System.out.println("Running tests: testEqualsAndNotEquals");

        assertEquals(6, demoUtils.add(2, 4), "2 + 4 must be 6");
        assertNotEquals(4, demoUtils.add(2, -2), "2 + 4 must not be 4");
    }

    @Test
    void testNullAndNotNull() {
        System.out.println("Running tests: testNullAndNotNull");

        String s1 = System.currentTimeMillis() < 3000 ? "3 secs" : null;
        String s2 = "testing";
        assertNull(demoUtils.checkNull(s1), "s1 is null");
        assertNotNull(demoUtils.checkNull(s2), "s2 is not null");
    }

    @Test
    void testSameAndNotSame() {
        String str = "Shaswati D";
        assertSame(demoUtils.getName(), demoUtils.getNameDupe(), "Objects should refer to same object");
        assertNotSame(str, demoUtils.getName(), "Objects should not refer to same object");
    }

    @Test
    void testTrueFalse() {
        int gradeOne = 10;
        int gradeTwo = 5;
        assertTrue(demoUtils.isGreater(gradeOne, gradeTwo), "should return true");
        assertFalse(demoUtils.isGreater(gradeTwo, gradeOne), "should return false");
    }

    @Test
    void testArrayEquals() {
        String[] alphabets = {"A", "B", "C"};
        assertArrayEquals(alphabets, demoUtils.getArrayAlphs(), "should return true");
    }

    @Test
    void testIterableEquals() {
        List<String> fieldList = List.of("Name", "Age", "City");
        assertIterableEquals(fieldList, demoUtils.getListData(), "should return true");
    }

    @Test
    void testThrowsAndDoesNotThrow() {
        assertThrows(Exception.class, () -> demoUtils.throwsException(-1),  "should throw Exception");
        assertDoesNotThrow(() -> demoUtils.throwsException(5), "should not throw Exception");
    }

    @Test
    void testTimeout() {
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> demoUtils.checkTimeout(),
                "Method should execute in 3 secs");
    }

    @AfterEach
    void setUpAfterEach() {
        System.out.println("Running @AfterEach");
    }

    @AfterAll
    static void setUpAfterAll() {
        System.out.println("@AfterAll executes once after all the test methods");
    }

    /* Method Order specify
        MethodOrderer.DisplayName - sorts test methods alphabetically
        MethodOrderer.MethodName - sorts test methods based on method names
        MethodOrderer.Random - random
        MethodOrderer.OrderAnnotation - sorts test methods numerically based on @Order() annotation
            - order with the lowest number has high priority
    */

    /*
         mvn clean test - remove old build files, compile your Java code, run the tests, provide a report if any tests fail
         mvn site -  generates a website with documentation about the project
         -DgenerateReports=false is a system property that disables report generation
    */
}
