package com.example.billboard.dto;

import com.example.billboard.model.User;
import com.example.billboard.model.enums.Action;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LogDto {
    private Long id;
    private Action action;
    private LocalDateTime timestamp;
    private User owner;
}
