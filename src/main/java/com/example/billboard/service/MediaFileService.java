package com.example.billboard.service;

import com.example.billboard.dto.MediaFileDto;
import com.example.billboard.mapper.MediaFileMapper;
import com.example.billboard.model.Advertisement;
import com.example.billboard.model.MediaFile;
import com.example.billboard.model.User;
import com.example.billboard.repository.AdvertisementRepository;
import com.example.billboard.repository.MediaFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MediaFileService {
    private final MediaFileRepository mediaFileRepository;
    private final AdvertisementRepository advertisementRepository;
    public void uploadFiles(MultipartFile[] files, User user) {
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    MediaFile mediaFile = MediaFile.builder()
                            .fileData(file.getBytes())
                            .owner(user)
                            .fileName(file.getOriginalFilename())
                            .fileSize(file.getSize())
                            .build();
                    mediaFileRepository.save(mediaFile);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
    }
    public String fileSizeToString(Long size) {
        if (size < 1024) {
            return size + " B";
        } else if (size < 1024 * 1024) {
            return String.format("%.2f KB", size / 1024.0);
        } else if (size < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", size / (1024.0 * 1024));
        } else if (size < 1024L * 1024 * 1024 * 1024) {
            return String.format("%.2f GB", size / (1024.0 * 1024 * 1024));
        } else {
            return String.format("%.2f GB", size / (1024.0 * 1024 * 1024));
        }
    }
    public List<MediaFileDto> finalAllByOwnerId(Long userId) {
        return mediaFileRepository.findAllByOwnerId(userId)
                .stream().map(MediaFileMapper::toMediaFileDto)
                .collect(Collectors.toList());
    }
    public int countMediaFilesByOwnerId(Long ownerId) {
        return mediaFileRepository.countAllByOwnerId(ownerId);
    }
    public MediaFileDto findFile(Long aLong) {
        return MediaFileMapper.toMediaFileDto(mediaFileRepository.findById(aLong).get());
    }
    public void deleteSelectedFiles(List<Long> selectedFilesIds) {
        if(selectedFilesIds == null) {
            return;
        }
        for (Long fileId : selectedFilesIds) {
            advertisementRepository.deleteAll(advertisementRepository.findByMediaFileId(mediaFileRepository.findById(fileId).get()));
            mediaFileRepository.findById(fileId).ifPresent(mediaFileRepository::delete);
        }
    }
}
