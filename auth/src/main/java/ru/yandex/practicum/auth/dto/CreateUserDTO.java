package ru.yandex.practicum.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserDTO {
    private String login;
    private String password;
}
