package com.example.DemoSelfTest1.controller;

import com.example.DemoSelfTest1.model.Student;
import com.example.DemoSelfTest1.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController//báo rằng đây là tầng controller = @Controller + @Respone
@RequestMapping("/customer")
public class AppController {

    @Autowired
    private AppService appService;

    public String test="succesful";


    //URL tới get Student
    @GetMapping("/Student")
    public List<String> getStudent(){
        //return appService.getStudent();
        return Arrays.asList("sdsa", "sadsa");
    }

    // POST: Thêm học sinh
    @PostMapping
    public Student addStudent(@RequestBody Student student) {
        return appService.addStudent(student);
    }

    // PUT: Cập nhật học sinh
    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable Long id, @RequestBody Student student) {
        return appService.updateStudent(id, student);
    }

    // DELETE: Xóa học sinh
    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable Long id) {
        appService.deleteStudent(id);
        return "Student với ID " + id + " đã đc xóa .";
    }




}
