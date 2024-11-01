package com.example.project.mvc.repository;

import com.example.project.mvc.models.MathGrade;
import org.springframework.data.repository.CrudRepository;

public interface MathGradesDao extends CrudRepository<MathGrade, Integer> {
    Iterable<MathGrade> findGradesByStudentId(int id);

    void deleteByStudentId(int id);
}
