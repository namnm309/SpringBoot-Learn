package com.example.SpringBootTurialVip.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name="tbl_cart")
public class Cart {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_user_id", referencedColumnName = "user_id", nullable = false)
	private User user;

	@ManyToOne
	private Product product;

	private Integer quantity;
	
	@Transient//dùng để lưu tạm thời , ko lưu vào database
	private Double totalPrice;
	
	@Transient
	private Double totalOrderPrice;

}
