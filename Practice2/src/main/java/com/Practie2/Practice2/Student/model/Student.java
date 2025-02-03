package com.Practie2.Practice2.Student.model;



import jakarta.persistence.*;

@Entity//ánh xạ class Student với bảng Student trong database
@Table (name = "Student")//Ánh xạ class này với bảng có tên Student
//@Getter
//@Setter
public class Student {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    private String name;
    private Integer age;
    private Integer year;

    public Student(){

    }

    public Student(int id, String name, int age, int year) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.year = year;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
