package com.example.Authentication.AppUser;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.swing.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

//Dùng lombok khai báo các setter và getter cũng như defalut constructor và EqualsAndHashCode

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode //Lombok => gộp 2 phương thức euqals() và hashCode()
    //equals(): so sánh các property của object để xem hay object có = nhau
    //hashCode(): tạo hashcode
@Entity //đây là 1 bảng trong database in second




//UserDetails thu vien co sang trong spring booot
public class appUser implements UserDetails {
//Implement cac Annotatione dinh nghia trong calss userdetails nhu phia duoi \
    //Define cac thuộc tính cần thiết ứng với các class dưới
    @Id
    @SequenceGenerator(
            name="student_squence",
            sequenceName = "student_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "student_sequence"
    )
    private Long id;
    private String name;
    private String username;
    private  String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private AppuserRole appUserRole;  //AppuserRole là 1 class enum => khởi tao class AppuserRole
    private Boolean enabled;    //Xem tài khoản đã đc mở hay lock
    private Boolean locked;     //Xem tài khoản đã đc mở hay lock

    //id sẽ đc JPA tự động tăng nên ko cần tạo constructor có tham số cho nó
    //=>Tạo constructor có tham số ko cần id


    public appUser(String name,
                   String username,
                   String email,
                   String password,
                   AppuserRole appUserRole,
                   Boolean enabled,
                   Boolean locked) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.appUserRole = appUserRole;
        this.enabled = enabled;
        this.locked = locked;
    }

    //Function cung cấp quyền truy cập
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority=new SimpleGrantedAuthority(appUserRole.name());
        return Collections.singletonList(authority);
    }

    @Override
    public String getPassword() {

        return password;
    }

    @Override
    public String getUsername() {

        return username;
    }

    @Override
    public boolean isAccountNonExpired() {

        return true;//ko usage trong turial này nhưng có hể manage
    }

    @Override
    public boolean isAccountNonLocked() {

        return !locked;//ko khóa
    }

    @Override
    public boolean isCredentialsNonExpired() {//Tài khoản còn hạn sử dụng :v ???
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;//tài khoản đc xác thực
    }
}
