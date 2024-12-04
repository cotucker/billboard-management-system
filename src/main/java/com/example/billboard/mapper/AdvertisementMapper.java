package com.example.billboard.mapper;

import com.example.billboard.dto.AdvertisementDto;
import com.example.billboard.model.Advertisement;

public interface AdvertisementMapper {
    static AdvertisementDto toAdvertisementDto(Advertisement advertisement) {
        return AdvertisementDto.builder()
                .id(advertisement.getId())
                .hostDevice(advertisement.getHostDevice())
                .minutesPerHour(advertisement.getMinutesPerHour())
                .title(advertisement.getTitle())
                .statistics(advertisement.getStatistics())
                .duration(advertisement.getDuration())
                .mediaFileId(advertisement.getMediaFileId())
                .build();
    }
}
