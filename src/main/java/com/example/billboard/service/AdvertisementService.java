package com.example.billboard.service;

import com.example.billboard.dto.AdvertisementDto;
import com.example.billboard.mapper.AdvertisementMapper;
import com.example.billboard.model.Advertisement;
import com.example.billboard.model.Device;
import com.example.billboard.model.MediaFile;
import com.example.billboard.repository.AdvertisementRepository;
import com.example.billboard.repository.DeviceRepository;
import com.example.billboard.repository.MediaFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdvertisementService {
    private final AdvertisementRepository advertisementRepository;
    private final DeviceRepository deviceRepository;
    private final MediaFileRepository mediaFileRepository;
    public List<AdvertisementDto> findAdvertisementsByHostDeviceId(Long deviceId) {
        List<Advertisement> advertisements = advertisementRepository.findByHostDeviceId(deviceId);
        return advertisements.stream().map(AdvertisementMapper::toAdvertisementDto).collect(Collectors.toList());
    }
    public AdvertisementDto findAdvertisementById(Long aLong) {
        return AdvertisementMapper.toAdvertisementDto(advertisementRepository.findById(aLong).get());
    }

    public Integer getAdvertisementCountByDeviceId(Long deviceId) {
       return advertisementRepository.getAdvertisementCount(deviceId);
    }
    public List<Advertisement> findAllAdvertisement() {
        return advertisementRepository.findAll();
    }
    public Long getNextAdvertisementId(List<AdvertisementDto> advertisements, Long currentAdvertisementId) {
        for (int i = 0; i < advertisements.size(); i++) {
                if(advertisements.get(i).getId().equals(currentAdvertisementId)) {
                    if(i == advertisements.size() - 1)
                        return advertisements.get(0).getId();
                    else
                        return advertisements.get(++i).getId();
            }
        }
        return advertisements.get(0).getId();
    }
    public byte[] getMediaFileDataByAdvrtisementId(Long advertisementID) {
        Optional<Advertisement> advertisementOptional = advertisementRepository.findById(advertisementID);
        Advertisement advertisement = advertisementOptional.orElseGet(() -> {
            throw new RuntimeException("Advertisement not found");
        });
        MediaFile mediaFile = advertisement.getMediaFileId();
        return mediaFile.getFileData();
    }
    public Integer getAllAdvertisementCount() {
        return advertisementRepository.getAllAdvertisementCount();
    }
    public Integer getAdvertisementCountByOwnerId(Long ownerId) {
        List<Device> devices = deviceRepository.findByOwnerId(ownerId);
        int advertisementCountByOwnerId = 0;
        for (Device device : devices) {
            advertisementCountByOwnerId += advertisementRepository.getAdvertisementCount(device.getId());
        }
        return advertisementCountByOwnerId;
    }
}
