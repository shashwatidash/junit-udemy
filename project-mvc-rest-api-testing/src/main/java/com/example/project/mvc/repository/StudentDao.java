package com.example.project.mvc.repository;

import com.example.project.mvc.models.CollegeStudent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentDao extends CrudRepository<CollegeStudent, Integer> {
    CollegeStudent findByEmailAddress(String mail);
}
