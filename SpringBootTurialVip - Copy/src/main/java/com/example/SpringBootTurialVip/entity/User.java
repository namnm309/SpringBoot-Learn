package com.example.SpringBootTurialVip.entity;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity//Đánh dấu đây là 1 table để map lên từ database
@Table(name="tbl_users")
// hibernate dưới jpa sẽ giúp chúng ta lm vc vs database
public class User implements UserDetails {
    @Id//Định nghĩa cho ID
    @GeneratedValue(strategy = GenerationType.IDENTITY)//Tránh bị scan ID
    @Column(name="user_id")
    private Long id;

    @Column(name="parent_id")
    private Long parentid;

    @Column(name="username")
    private String username;

    @Column(name="fullname")
    private String fullname;

    @Column(name="password")
    private String password;

    @Column(name="email")
    private String email;

    @Column(name="phone")
    private String phone;

    @Column(name="birth_date")
    private Date bod;

    @Column(name="gender")
    private String gender;

    @Column(name="enabled")
    private boolean enabled;

    @Column(name="verification_cod")
    private String verification_code;

    @Column(name="verification_expired")
    private LocalDateTime verification_expired;

    @Column(name="verification_expiration")
    private LocalDateTime verfication_expiration;

    public User() {
    }

    //Constructor cho việc sign up
    public User(String password, String username, String email) {
        this.password = password;
        this.username = username;
        this.email = email;
    }

    public User(Long id, Long parentid, String username, String fullname, String password, String email, String phone, Date bod, String gender, boolean enabled) {
        this.id = id;
        this.parentid = parentid;
        this.username = username;
        this.fullname = fullname;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.bod = bod;
        this.gender = gender;
        this.enabled = enabled;
    }

    public User(Long id, Long parentid, String username, String fullname, String password, String email, String phone, Date bod, String gender, boolean enabled, String verification_code, LocalDateTime verification_expired, LocalDateTime verfication_expiration) {
        this.id = id;
        this.parentid = parentid;
        this.username = username;
        this.fullname = fullname;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.bod = bod;
        this.gender = gender;
        this.enabled = enabled;
        this.verification_code = verification_code;
        this.verification_expired = verification_expired;
        this.verfication_expiration = verfication_expiration;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentid() {
        return parentid;
    }

    public void setParentid(Long parentid) {
        this.parentid = parentid;
    }

    public String getUsername() {
        return username;
    }

    //Account đã hết hạn chưa
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //Account có bị khóa không
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //Account đã dược xác thực ?
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

//    @Override
//    public boolean isEnabled(){
//        return enabled;
//    }

//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return enabled;
//    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    //Hàm sinh ra do implement UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getBod() {
        return bod;
    }

    public void setBod(Date bod) {
        this.bod = bod;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getVerification_code() {
        return verification_code;
    }

    public void setVerification_code(String verification_code) {
        this.verification_code = verification_code;
    }

    public LocalDateTime getVerification_expired() {
        return verification_expired;
    }

    public void setVerification_expired(LocalDateTime verification_expired) {
        this.verification_expired = verification_expired;
    }

    public LocalDateTime getVerfication_expiration() {
        return verfication_expiration;
    }

    public void setVerfication_expiration(LocalDateTime verfication_expiration) {
        this.verfication_expiration = verfication_expiration;
    }
}
