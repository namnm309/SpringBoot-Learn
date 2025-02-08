package com.Practie2.Practice2.Student.service;

import com.Practie2.Practice2.Student.model.Student;
import com.Practie2.Practice2.Student.repository.StudentReposistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Map dữ liệu trong db
 */
@Service //Giúp Spring nhận biết đc beans này trong constructor tạo bên controller
public class StudentService {

    @Autowired
    private StudentReposistory studentReposistory;

//    public StudentService(StudentReposistory studentRepository) {
//
//        this.studentReposistory = studentRepository;
//    }

    public List<Student> getStudent(){
//        Student st = new Student(10,"mnam", 20,2024);
//        return List.of(st);

        List<Student>  ans= studentReposistory.findAll();
        return ans;
    }
}
