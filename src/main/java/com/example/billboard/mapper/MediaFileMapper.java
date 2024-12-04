package com.example.billboard.mapper;

import com.example.billboard.dto.MediaFileDto;
import com.example.billboard.model.MediaFile;

public interface MediaFileMapper {
    static MediaFileDto toMediaFileDto(MediaFile mediaFile){
        return MediaFileDto.builder()
                .fileData(mediaFile.getFileData())
                .owner(mediaFile.getOwner())
                .fileSize(mediaFile.getFileSize())
                .fileName(mediaFile.getFileName())
                .id(mediaFile.getId())
                .build();
    }
}
