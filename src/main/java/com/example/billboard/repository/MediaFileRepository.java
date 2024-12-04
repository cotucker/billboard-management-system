package com.example.billboard.repository;

import com.example.billboard.model.MediaFile;
import com.example.billboard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaFileRepository extends JpaRepository<MediaFile, Long> {
    List<MediaFile> findAllByOwnerId(Long  ownerId);
    Integer countAllByOwnerId(Long ownerId);
}
