package ru.yandex.practicum.shop.service;

import reactor.core.publisher.Mono;
import ru.yandex.practicum.shop.dto.user.CreateUserDTO;
import ru.yandex.practicum.shop.dto.user.UserResponseDTO;

public interface UserService {
    Mono<UserResponseDTO> save(CreateUserDTO data);

    Mono<UserResponseDTO> findById(Long id);
}
