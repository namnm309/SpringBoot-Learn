package com.example.DemoSelfTest1.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "tbl_users")
public class tbl_users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true, length = 50)
    private String username;

    @Column(length = 100)
    private String fullname;

    @Column(length = 255)
    private String password;

    @Column(unique = true, length = 50)
    private String email;

    @Column(unique = true, length = 50, nullable = false)
    private String phone;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    // Giả sử bạn lưu giá trị gender dạng text ('male' hoặc 'female')
    @Column(nullable = false)
    private String gender;

    // Quan hệ ManyToMany với TblRole thông qua bảng trung gian tbl_users_role
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "tbl_users_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<tbl_users> roles = new HashSet<>();


}
