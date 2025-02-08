package com.nofear.nac.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;
    private String password;

//    @Enumerated(EnumType.STRING)
//    private EncryptionAlgorithm algorithm;

    //Quan he voi bang authority de lay ra phan quyen cua user
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_authority", // Join table name
            joinColumns = @JoinColumn(name = "user_id"), // Foreign key for User
            inverseJoinColumns = @JoinColumn(name = "authority_id") // Foreign key for Authority
    )
    private List<Authority> authorities = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

//    public EncryptionAlgorithm getAlgorithm() {
//        return algorithm;
//    }
//
//    public void setAlgorithm(EncryptionAlgorithm algorithm) {
//        this.algorithm = algorithm;
//    }

    public List<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", authorities=" + authorities +
                '}';
    }
}
