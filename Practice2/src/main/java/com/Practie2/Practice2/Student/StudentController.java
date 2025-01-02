package com.Practie2.Practice2.Student;
//API Layer

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@RestController//Anotation này dùng để cho các endpoint khác chạy , nói caách khác cho class này là restful api
@RequestMapping ("/api/v1")
public class StudentController {

    @Autowired
    private StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

//    @Autowired
//    public StudentController(StudentService studentService) {
//        this.studentService = studentService;
//    }
//Ko sử dụng Autowired
//    public StudentController(StudentService studentService) {
//        this.studentService = studentService;
//    }

    @GetMapping("/getStudent")
    public List<Student> getStudent(){
       return studentService.getStudent();
    }



}
