package com.example.billboard.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "advertisements")
public class Advertisement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private Integer duration;
    private Float minutesPerHour;
    @ManyToOne
    @JoinColumn(name = "device_id", nullable = false)
    private Device hostDevice;
    private Integer statistics;
    @ManyToOne
    @JoinColumn(name = "media_file_id", nullable = true)
    private MediaFile mediaFileId;
}