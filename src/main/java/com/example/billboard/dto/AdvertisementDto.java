package com.example.billboard.dto;

import com.example.billboard.model.Device;
import com.example.billboard.model.MediaFile;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdvertisementDto {
        private Long id;
        private String title;
        private Integer duration;
        private Float minutesPerHour;
        private Device hostDevice;
        private Integer statistics;
        private MediaFile mediaFileId;
}
