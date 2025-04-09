package ru.yandex.practicum.shop.utils;

import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.shop.model.User;

public class SecurityUtils {
    public static Mono<Long> getUserId() {
        return ReactiveSecurityContextHolder
                .getContext()
                .map(SecurityContext::getAuthentication)
                .switchIfEmpty(Mono.empty())
                .map(p -> (User) p.getPrincipal())
                .map(User::getId);
    }
}
