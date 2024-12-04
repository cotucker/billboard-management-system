package com.example.billboard.repository;

import com.example.billboard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long aLong);
    @Query(nativeQuery = true,
            value = """
            SELECT COUNT(*)
            FROM users
            """)
    Integer getUsersCount();

    Integer countAllByUsername(String username);
    Optional<User> findByUsername(String username);
}
