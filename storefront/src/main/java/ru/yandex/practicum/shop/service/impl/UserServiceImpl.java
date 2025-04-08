package ru.yandex.practicum.shop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.shop.dto.user.CreateUserDTO;
import ru.yandex.practicum.shop.dto.user.UserResponseDTO;
import ru.yandex.practicum.shop.exception.ResourceNotFoundException;
import ru.yandex.practicum.shop.mapper.UserMapper;
import ru.yandex.practicum.shop.repository.UserRepository;
import ru.yandex.practicum.shop.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, ReactiveUserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByLogin(username)
                .map(u -> (UserDetails) u);
    }

    @Override
    public Mono<UserResponseDTO> save(CreateUserDTO data) {
        var user = userMapper.map(data);
        return userRepository.save(user)
                .map(userMapper::map);
    }

    @Override
    public Mono<UserResponseDTO> findById(Long id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Пользователь", id)))
                .map(userMapper::map);
    }
}
