package com.example.project.mvc.repository;

import com.example.project.mvc.models.ScienceGrade;
import org.springframework.data.repository.CrudRepository;

public interface ScienceGradesDao extends CrudRepository<ScienceGrade, Integer>  {
    Iterable<ScienceGrade> findGradesByStudentId(int id);

    void deleteByStudentId(int id);
}
