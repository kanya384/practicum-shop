package ru.yandex.practicum.auth.mapper;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.yandex.practicum.auth.dto.CreateUserDTO;
import ru.yandex.practicum.auth.dto.UserResponseDTO;
import ru.yandex.practicum.auth.model.User;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
@RequiredArgsConstructor
public abstract class UserMapper {
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User map(CreateUserDTO data) {
        return User.builder()
                .login(data.getLogin())
                .password(passwordEncoder.encode(data.getPassword()))
                .role("ROLE_CUSTOMER")
                .build();
    }

    public abstract UserResponseDTO map(User user);
}
