package com.example.SpringBootTurialVip.dto.request;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500,unique = true)
    private String title;

    @Column(length = 5000)
    private String description;

    @Column(unique = true)
    private String category;

    private Double price;

    private int stock;

    private int discount;

    private Double discountPrice;

    private Boolean isActive;
}
