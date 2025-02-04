package com.nofear.nac.repositories;

import com.nofear.nac.entities.Authority;
import com.nofear.nac.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Integer> {
    Optional<Authority> findUserByAuthority(String authority);
}
