package org.example.service;

import org.example.dao.ApplicationDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ApplicationService {
    @Autowired
    private ApplicationDao dao;

    public Double addResultsForSingleClass(List<Double> numbers) {
        return dao.addGradeResultsForSingleClass(numbers);
    }

    public Double findGradePointAverage(List<Double> grades) {
        return dao.findGradePointAverage(grades);
    }

    public Object checkNull(Object obj) {
        return dao.checkNull(obj);
    }
}
