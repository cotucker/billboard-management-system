package com.example.billboard.mapper;

import com.example.billboard.dto.LogDto;
import com.example.billboard.model.Log;

public interface LogMapper {
    static LogDto toLogDto(Log log){
        return LogDto.builder()
                .id(log.getId())
                .action(log.getAction())
                .owner(log.getOwner())
                .timestamp(log.getTimestamp())
                .build();
    }

    static Log toLog(LogDto log){
        return Log.builder()
                .id(log.getId())
                .action(log.getAction())
                .owner(log.getOwner())
                .timestamp(log.getTimestamp())
                .build();
    }
}
