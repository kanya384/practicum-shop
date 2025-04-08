package ru.yandex.practicum.shop.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserDTO {
    private String login;
    private String password;
}
