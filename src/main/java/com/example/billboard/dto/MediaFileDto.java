package com.example.billboard.dto;

import com.example.billboard.model.Advertisement;
import com.example.billboard.model.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MediaFileDto {
    private Long id;
    private byte[] fileData;
    private User owner;
    private String fileName;
    private Long fileSize;
}
