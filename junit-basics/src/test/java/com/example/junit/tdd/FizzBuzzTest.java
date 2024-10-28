package com.example.junit.tdd;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FizzBuzzTest {

    // If number is divisible by 3, print Fizz
    @Test
    @DisplayName("DivisibleByThree")
    @Order(1)
    void testForDivisibleByThree() {
        String expected = "Fizz";
        assertEquals(expected, FizzBuzz.compute(3), "should return fizz");
    }

    // If number is divisible by 5, print Buzz
    @Test
    @DisplayName("DivisibleByFive")
    @Order(2)
    void testForDivisibleByFive() {
        String expected = "Buzz";
        assertEquals(expected, FizzBuzz.compute(5), "should return buzz");
    }

    // If number is divisible by 3 and 5, print FizzBuzz
    @Test
    @DisplayName("DivisibleByThreeAndFive")
    @Order(3)
    void testForDivisibleByThreeAndFive() {
        String expected = "FizzBuzz";
        assertEquals(expected, FizzBuzz.compute(15), "should return fizz buzz");
    }

    // If number is not divisible by 3 and 5, print the number
    @Test
    @DisplayName("NotDivisibleByThreeOrFive")
    @Order(4)
    void testForNotDivisibleByThreeOrFive() {
        String expected = "28";
        assertEquals(expected, FizzBuzz.compute(28), "should return num value");
    }

    @ParameterizedTest(name = "val={0}, expected={1}")
    @DisplayName("Testing with CSV data")
    @CsvSource({
            "1,1",
            "2,2",
            "3,Fizz",
            "4,4",
            "5,Buzz",
            "6,Fizz",
            "10,Buzz",
            "15,FizzBuzz",
            "30,FizzBuzz"
    })
    @Order(6)
    void testCsvData(int val, String expected) {
        assertEquals(expected, FizzBuzz.compute(val));
    }

    /* Replaced by @ParameterizedTest */
    @Test
    @DisplayName("LoopOverExamples")
    @Order(5)
    void testLoopOverSetOfExamples() {
        String[][] data = {
                {"1", "1"},
                {"2", "2"},
                {"3", "Fizz"},
                {"4", "4"},
                {"5", "Buzz"},
                {"6", "Fizz"},
                {"10", "Buzz"},
                {"15", "FizzBuzz"},
                {"30", "FizzBuzz"},
        };

        for (String[] val : data) {
            assertEquals(val[1], FizzBuzz.compute(Integer.parseInt(val[0])), "should return " + val[1]);
        }
    }

    @ParameterizedTest(name = "val={0}, expected={1}")
    @DisplayName("Testing with .CSV file data")
    @CsvFileSource(resources = "/large-test-data.csv")
    @Order(7)
    void testCsvFileData(int val, String expected) {
        assertEquals(expected, FizzBuzz.compute(val));
    }
}
