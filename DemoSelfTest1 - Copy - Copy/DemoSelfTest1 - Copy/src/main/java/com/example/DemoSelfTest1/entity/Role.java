package com.example.DemoSelfTest1.entity;

import com.example.DemoSelfTest1.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Entity
@Table(name = "roles")
//@Getter
//@Setter
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="role_id")
    private Long id;

    // Ví dụ: "ADMIN", "USER", "STAFF", ...
    @Column(name = "role_name", unique = true, nullable = false, length = 50)
    private String roleName;

    // Nếu cần, bạn có thể khai báo quan hệ ManyToMany với User (mặt định là optional)
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
