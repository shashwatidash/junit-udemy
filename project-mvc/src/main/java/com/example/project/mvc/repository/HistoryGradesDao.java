package com.example.project.mvc.repository;

import com.example.project.mvc.models.HistoryGrade;
import org.springframework.data.repository.CrudRepository;

public interface HistoryGradesDao extends CrudRepository<HistoryGrade, Integer> {
    Iterable<HistoryGrade> findGradesByStudentId(int id);

    void deleteByStudentId(int id);
}
