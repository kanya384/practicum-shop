package ru.yandex.practicum.auth.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserResponseDTO {
    private Long id;
    private String login;
    private LocalDate createdAt;
    private LocalDate modifiedAt;
}
