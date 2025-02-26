package com.example.SpringBootTurialVip.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "tbl_invalidtoken")
public class InvalidatedToken {
    @Id
    String id;

    Date expiryTime;//quét và xóa đi các token log out đã hết hạn trong đây để db đc trống
}
