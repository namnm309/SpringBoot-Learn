package com.Practie2.Practice2.Student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Service //Giúp Spring nhận biết đc beans này trong constructor tạo bên controller
public class StudentService {

    @Autowired
    private StudentReposistory studentReposistory;

    public StudentService(StudentReposistory studentRepository) {
        this.studentReposistory = studentRepository;
    }

    public List<Student> getStudent(){
//        Student st = new Student(10,"mnam", 20,2024);
//        return List.of(st);

        List<Student>  ans= studentReposistory.findAll();
        return ans;
    }
}
