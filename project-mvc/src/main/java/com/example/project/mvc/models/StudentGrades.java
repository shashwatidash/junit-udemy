package com.example.project.mvc.models;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
public class StudentGrades {

    private List<Grade> mathGradesResult;

    private List<Grade> scienceGradesResult;

    private List<Grade> historyGradesResult;

    public StudentGrades() {}

    public double addGradeResultsForSingleClass(List<Double> grades) {
        double result = 0;
        for (double i : grades) {
            result += i;
        }
        return result;
    }

    public double findGradePointAverage(List<Double> grades ) {
        int lengthOfGrades = grades.size();
        double sum = addGradeResultsForSingleClass(grades);
        double result = sum / lengthOfGrades;

        // add a round function
        BigDecimal resultRound = BigDecimal.valueOf(result);
        resultRound = resultRound.setScale(2, RoundingMode.HALF_UP);
        return resultRound.doubleValue();
    }

    public Boolean isGradeGreater(double gradeOne, double gradeTwo) {
        return gradeOne > gradeTwo;
    }

    public Object checkNull(Object obj) {
        return obj;
    }

    public List<Grade> getMathGradesResult() {
        return mathGradesResult;
    }

    public void setMathGradesResult(List<Grade> mathGradesResult) {
        this.mathGradesResult = mathGradesResult;
    }

    public List<Grade> getScienceGradesResult() {
        return scienceGradesResult;
    }

    public void setScienceGradesResult(List<Grade> scienceGradesResult) {
        this.scienceGradesResult = scienceGradesResult;
    }

    public List<Grade> getHistoryGradesResult() {
        return historyGradesResult;
    }

    public void setHistoryGradesResult(List<Grade> historyGradesResult) {
        this.historyGradesResult = historyGradesResult;
    }

    @Override
    public String toString() {
        return "StudentGrades{" +
                "mathGradesResult=" + mathGradesResult +
                ", scienceGradesResult=" + scienceGradesResult +
                ", historyGradesResult=" + historyGradesResult +
                '}';
    }
}
