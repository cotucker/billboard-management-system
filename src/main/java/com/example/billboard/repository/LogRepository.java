package com.example.billboard.repository;

import com.example.billboard.model.Log;
import com.example.billboard.model.User;
import com.example.billboard.model.enums.Action;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface LogRepository extends JpaRepository<Log, Long> {
    List<Log> findAllByAction(Action action);
    List<Log> findAllByOwner(User user);
}
