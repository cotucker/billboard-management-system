package com.example.billboard.service;

import com.example.billboard.dto.DeviceDto;
import com.example.billboard.mapper.DeviceMapper;
import com.example.billboard.model.Advertisement;
import com.example.billboard.model.Device;
import com.example.billboard.model.MediaFile;
import com.example.billboard.model.User;
import com.example.billboard.repository.AdvertisementRepository;
import com.example.billboard.repository.DeviceRepository;
import com.example.billboard.repository.MediaFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class DeviceService {
    private final DeviceRepository deviceRepository;
    private final MediaFileRepository mediaFileRepository;
    private final AdvertisementRepository advertisementRepository;
    private Map<Long, Long> deviceNextFile;

    public List<DeviceDto> findAllDevices() {
        return deviceRepository.findAll().stream()
                .map(DeviceMapper::toDeviceDto)
                .collect(Collectors.toList());
    }
    public List<DeviceDto> findAllDevicesWithAdvertisement(){
        List<Device> devices = deviceRepository.findAll();
        return devices.stream()
                .filter(device -> advertisementRepository.getAdvertisementCount(device.getId()) != 0)
                .map(DeviceMapper::toDeviceDto)
                .collect(Collectors.toList());
    }

    public List<DeviceDto> findUngroupedDevices(Long userid) {
        return deviceRepository.findByDeviceGroupIDIsNullAndOwnerId(userid).stream()
                .map(DeviceMapper::toDeviceDto)
                .collect(Collectors.toList());
    }
    public List<DeviceDto> findAllUngroupedDevices() {
        return deviceRepository.findAllByDeviceGroupIDIsNull().stream()
                .map(DeviceMapper::toDeviceDto)
                .collect(Collectors.toList());
    }
    public List<DeviceDto> findDevicesByUserId(Long userId) {
        List<Device> devices = deviceRepository.findByOwnerId(userId);
        return devices.stream()
                .map(DeviceMapper::toDeviceDto)
                .collect(Collectors.toList());
    }

    public void addDevice(DeviceDto newDevice, User oldUser) {
        if(!newDevice.getName().isEmpty()) {
            Device device = DeviceMapper.toDevice(newDevice);
            device.setOwner(oldUser);
            device.setUsedStorage(0L);
            device.setStorageLimit(newDevice.getStorageLimit()*(1024*1024*1024));
            deviceRepository.save(device);
        }
    }
    public Integer getAllDeviceCount() {
        return deviceRepository.getAllDeviceCount();
    }
    public Integer getDeviceCountByOwnerId(Long ownerId) {
        return deviceRepository.getDeviceCountByOwnerId(ownerId);
    }
    public void groupDevicesById(List<Long> selectedDeviceIds){
        if(selectedDeviceIds == null) {
            return;
        }
        Long nextGroupNumber;
        if(deviceRepository.countDevicesWithDeviceGroupIDNotNull() > 0) {
            nextGroupNumber = deviceRepository.getMinUsedGroupNumber() == 1 ?
                deviceRepository.getNextGroupNumber() : deviceRepository.getMinUsedGroupNumber() - 1;
        }else {
            nextGroupNumber = 1L;
        }
        for (Long deviceId : selectedDeviceIds) {
            Device device = deviceRepository.findById(deviceId).orElse(null);
            if (device != null) {
                device.setDeviceGroupID(nextGroupNumber);
                deviceRepository.save(device);
            }
        }
    }
    public Map<Long, List<DeviceDto>> getDevicesGroupedByGroupNumber(long userid) {
        List<DeviceDto> devices = deviceRepository.findByOwnerId(userid).stream()
                .map(DeviceMapper::toDeviceDto)
                .toList();
        Map<Long, List<DeviceDto>> devicesGroupedByGroupNumber = new HashMap<>();
        for (DeviceDto device : devices) {
            Long groupNumber = device.getDeviceGroupID();
            if (groupNumber != null) {
                devicesGroupedByGroupNumber.computeIfAbsent(groupNumber, k -> new ArrayList<>()).add(device);
            }
        }
        return devicesGroupedByGroupNumber;
    }
    public Map<Long, List<DeviceDto>> getAllDevicesGroupedByGroupNumber() {
        List<DeviceDto> devices = deviceRepository.findAll().stream()
                .map(DeviceMapper::toDeviceDto)
                .toList();
        Map<Long, List<DeviceDto>> devicesGroupedByGroupNumber = new HashMap<>();
        for (DeviceDto device : devices) {
            Long groupNumber = device.getDeviceGroupID();
            if (groupNumber != null) {
                devicesGroupedByGroupNumber.computeIfAbsent(groupNumber, k -> new ArrayList<>()).add(device);
            }
        }
        return devicesGroupedByGroupNumber;
    }
    public void deleteSelectedDevices(List<Long> selectedDeviceIds) {
        for (Long deviceId : selectedDeviceIds) {
            advertisementRepository.deleteAll(advertisementRepository.findByHostDeviceId(deviceId));
            deviceRepository.findById(deviceId).ifPresent(deviceRepository::delete);
        }
    }

    public void assignAdverstismentToDevice(List<Long> selectedDeviceGroupIds, List<Long> selectedDeviceIds, List<Long> selectedFilesIds) {
        List<Device> devices = new ArrayList<>();
        if(selectedDeviceGroupIds != null) {
            for (Long deviceGroupId: selectedDeviceGroupIds) {
                devices.addAll(deviceRepository.findByDeviceGroupID(deviceGroupId));
            }
        }
        if(selectedDeviceIds != null) {
            for (Long deviceId : selectedDeviceIds) {
                devices.add(deviceRepository.findById(deviceId).orElse(null));
            }
        }
        if(!devices.isEmpty()) {
            for (Device device : devices) {
                device.setUsedStorage(0L);
                List<Advertisement> advertisements = advertisementRepository.findByHostDeviceId(device.getId());
                advertisementRepository.deleteAll(advertisements);
                }
        }
        if(selectedFilesIds == null) {
            return;
        }

        for (Device device : devices) {
            long usedStorage = device.getUsedStorage();
            long deviceStorageLimit = device.getStorageLimit();

            for (Long selectedFileId : selectedFilesIds) {
                MediaFile mediaFile = mediaFileRepository.findById(selectedFileId).orElseThrow(() -> new RuntimeException("Media file not found"));

                if (usedStorage + mediaFile.getFileSize() <= deviceStorageLimit) {
                    Advertisement advertisement = Advertisement.builder()
                            .hostDevice(device)
                            .mediaFileId(mediaFile)
                            .build();
                    advertisementRepository.save(advertisement);
                    usedStorage += mediaFile.getFileSize();
                } else {
                    break;
                }
            }
            device.setUsedStorage(usedStorage);
            deviceRepository.save(device);
        }
    }
}
