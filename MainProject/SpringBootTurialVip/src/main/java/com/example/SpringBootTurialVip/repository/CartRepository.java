package com.example.SpringBootTurialVip.repository;


import com.example.SpringBootTurialVip.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {

	public Cart findByProductIdAndUserId(Long productId, Long userId);

	public Long countByUserId(Long userId);

	@Query("SELECT c FROM Cart c WHERE c.user.id = :userId")
	public List<Cart> findByUserId(Long userId);

}
