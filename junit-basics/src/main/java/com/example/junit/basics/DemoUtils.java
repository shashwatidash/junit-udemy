package com.example.junit.basics;

import java.util.List;

public class DemoUtils {

    private String name = "Shaswati";
    private String nameDupe = name;
    private String[] arrayAlphs = {"A", "B", "C"};
    private List<String> listData = List.of("Name", "Age", "City");

    public String[] getArrayAlphs() {
        return arrayAlphs;
    }

    public List<String> getListData() {
        return listData;
    }

    public String getName() {
        return name;
    }

    public String getNameDupe() {
        return nameDupe;
    }

    public int add(int a, int b) {
        return a + b;
    }

    public int multiply(int a, int b) {
        return a * b;
    }

    public Boolean isGreater(int n1, int n2) {
        return n1 > n2;
    }

    public Object checkNull(Object ob) {
        if (ob != null) {
            return ob;
        }
        return null;
    }

    public String throwsException(int a) throws Exception {
        if (a < 0) {
            throw new Exception("Values should be greater than or equal to 0");
        }
        return "Value is greater than or equal to 0";
    }

    public void checkTimeout() throws InterruptedException {
        System.out.println("I am going to sleep");
        Thread.sleep(2000);
        System.out.println("Sleeping over");
    }
}
