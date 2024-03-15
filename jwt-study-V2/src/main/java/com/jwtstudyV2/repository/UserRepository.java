package com.jwtstudyV2.repository;

import com.jwtstudyV2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User , Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByRefreshToken(String refreshToken); // 문제 생기는지 잘 확인

}
