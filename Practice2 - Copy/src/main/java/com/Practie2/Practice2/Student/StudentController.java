package com.Practie2.Practice2.Student;
//API Layer

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@RestController//Anotation này dùng để cho các endpoint khác chạy , nói caách khác cho class này là restful api
@RequestMapping ("/api/v1")
public class StudentController {

    //Annotation Autowired tự động inject các dependency cần thiết vào constructor của bean
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

    //Tạo API lấy thông tin sinh viên
    @GetMapping("/getStudent")
    public List<Student> getStudent(){

        return studentService.getStudent();
    }

    // API thêm sinh viên mới
    @PostMapping("/addStudent")
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        Student newStudent = studentService.addStudent(student);
        return new ResponseEntity<>(newStudent, HttpStatus.CREATED);
    }

    //


}
