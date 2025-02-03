package com.example.DemoSelfTest1.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "tbl_roles")
public class tbl_role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;

    @Column(name = "role_name", unique = true, length = 50)
    private String roleName;

    // Nếu cần, bạn có thể map quan hệ ManyToMany ngược lại
    @ManyToMany(mappedBy = "roles")
    private Set<tbl_role> users = new HashSet<>();


}

