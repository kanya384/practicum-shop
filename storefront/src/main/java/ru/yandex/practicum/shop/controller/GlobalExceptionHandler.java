package ru.yandex.practicum.shop.controller;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.shop.exception.*;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({NotEnoughMoneyException.class})
    @ResponseStatus(HttpStatus.PAYMENT_REQUIRED)
    public Mono<String> handleBNotEnoughMoneyException(Exception e, Model model) {
        model.addAttribute("error", Map.of(
                "code", HttpStatus.PAYMENT_REQUIRED,
                "text", e.getMessage()
        ));
        return Mono.just("oops");
    }

    @ExceptionHandler({AlreadyExistsInCartException.class, NoProductsInOrderException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<String> handleBadRequestException(Exception e, Model model) {
        model.addAttribute("error", Map.of(
                "code", HttpStatus.BAD_REQUEST,
                "text", e.getMessage()
        ));
        return Mono.just("oops");
    }

    @ExceptionHandler({ResourceNotFoundException.class, NoItemInCartException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<String> handleResourceNotFoundException(Exception e, Model model) {
        model.addAttribute("error", Map.of(
                "code", HttpStatus.NOT_FOUND,
                "text", e.getMessage()
        ));
        return Mono.just("oops");
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception e, Model model) {
        model.addAttribute("error", Map.of(
                "code", HttpStatus.INTERNAL_SERVER_ERROR,
                "text", e.getMessage()
        ));
        return "oops";
    }
}
