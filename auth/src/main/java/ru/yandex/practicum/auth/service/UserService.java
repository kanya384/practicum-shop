package ru.yandex.practicum.auth.service;

import reactor.core.publisher.Mono;
import ru.yandex.practicum.auth.dto.CreateUserDTO;
import ru.yandex.practicum.auth.dto.UserResponseDTO;

public interface UserService {
    Mono<UserResponseDTO> save(CreateUserDTO data);

    Mono<UserResponseDTO> findById(Long id);
}
