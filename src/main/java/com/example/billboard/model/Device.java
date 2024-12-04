package com.example.billboard.model;

import com.example.billboard.model.enums.DeviceStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "devices")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long storageLimit;
    private Long usedStorage;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;
    private Long deviceGroupID;
    @Enumerated(EnumType.STRING)
    private DeviceStatus status;
}
