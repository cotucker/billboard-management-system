package com.example.billboard.dto;

import com.example.billboard.model.User;
import com.example.billboard.model.enums.DeviceStatus;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeviceDto {
    private Long id;
    private String name;
    private Long storageLimit;
    private Long usedStorage;
    private User owner;
    private Long deviceGroupID;
    private DeviceStatus status;
}
