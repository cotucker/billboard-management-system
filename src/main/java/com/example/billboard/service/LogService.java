package com.example.billboard.service;

import com.example.billboard.dto.LogDto;
import com.example.billboard.mapper.LogMapper;
import com.example.billboard.model.Log;
import com.example.billboard.model.User;
import com.example.billboard.model.enums.Action;
import com.example.billboard.repository.AdvertisementRepository;
import com.example.billboard.repository.DeviceRepository;
import com.example.billboard.repository.LogRepository;
import com.example.billboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LogService {
    private final LogRepository logRepository;
    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;
    private final AdvertisementRepository advertisementRepository;

    public void saveLog(User user, LocalDateTime timestamp, Action action){
        logRepository.save(Log.builder()
                .owner(user)
                .timestamp(timestamp)
                .action(action)
                .build());
    }
    public void saveSetOfLog(User user, LocalDateTime timestamp, Action action, long size){
        for (int i = 0; i < size; i++) {
        logRepository.save(Log.builder()
                .owner(user)
                .timestamp(timestamp)
                .action(action)
                .build());
        }
    }
    public List<LogDto> findAllLogs(){
       return logRepository.findAll().stream().map(LogMapper::toLogDto).collect(Collectors.toList());
    }
    public ResponseEntity<Resource> exportLogsToFile(String action)  throws IOException {
        List<Log> logs;
        if(action == null) {
            logs = logRepository.findAll();
        }else {
            logs = logRepository.findAllByAction(Action.valueOf(action));
        }
        File tempFile = File.createTempFile("user-logs", ".txt");
        try (FileWriter writer = new FileWriter(tempFile)) {
            for (Log log : logs) {
                writer.write(log.getTimestamp() + " | " + log.getOwner().getId() + " | " + log.getAction() + "\n");
            }
        }
        Resource resource = new FileSystemResource(tempFile);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + tempFile.getName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
