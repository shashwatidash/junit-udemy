package com.example.junit.tdd;

public class FizzBuzz {

    // If number is divisible by 3, print Fizz
    // If number is divisible by 5, print Buzz
    // If number is divisible by 3 and 5, print FizzBuzz
    // If number is not divisible by 3 and 5, print the number

    public static String compute(int val) {
        StringBuilder str = new StringBuilder();
        if (val % 3 == 0) str.append("Fizz");
        if (val % 5 == 0) str.append("Buzz");
        if (str.isEmpty()) str.append(val);
        return str.toString();
    }

/* old impl
    public static String compute(int val) {
        if (val % 3 == 0 & val % 5 == 0) return "FizzBuzz";
        else if (val % 3 == 0) return "Fizz";
        else if (val % 5 == 0) return "Buzz";
        return Integer.toString(val);
    }
*/
}
