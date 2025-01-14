package com.tech.petfriends.community.repository;

import java.util.List;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<User, Long> {
    // 필요한 커스텀 쿼리 메서드 추가 가능
    List<User> findByUsername(String username);
}
