package com.Practie2.Practice2.Student.repository;

import com.Practie2.Practice2.Student.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository//Báo đây là class query data
public interface StudentReposistory
        extends JpaRepository<Student,Integer> {

}
