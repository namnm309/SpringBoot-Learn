package entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;

@Entity
@Table(name = "tbl_staff")
@Data
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long staffId;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String phone;

    @Column(nullable = false)
    private String position;

    private Double salary;

    @Column(nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private KafkaProperties.Admin admin; // Quan hệ với bảng Admin
}
