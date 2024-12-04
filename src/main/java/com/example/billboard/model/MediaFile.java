package com.example.billboard.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "mediafiles")
public class MediaFile {
    @Id
    @GeneratedValue
    private Long id;
    private byte[] fileData;
    private String fileName;
    private Long fileSize;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;
}
