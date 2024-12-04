package com.example.billboard.mapper;

import com.example.billboard.dto.DeviceDto;
import com.example.billboard.model.Device;

public interface DeviceMapper {
    static DeviceDto toDeviceDto(Device device) {
        return DeviceDto.builder()
                .id(device.getId())
                .deviceGroupID(device.getDeviceGroupID())
                .name(device.getName())
                .storageLimit(device.getStorageLimit())
                .usedStorage(device.getUsedStorage())
                .owner(device.getOwner())
                .status(device.getStatus())
                .build();
    }
    static Device toDevice(DeviceDto device) {
        return Device.builder()
                .id(device.getId())
                .deviceGroupID(device.getDeviceGroupID())
                .name(device.getName())
                .storageLimit(device.getStorageLimit())
                .usedStorage(device.getUsedStorage())
                .owner(device.getOwner())
                .status(device.getStatus())
                .build();
    }
}
