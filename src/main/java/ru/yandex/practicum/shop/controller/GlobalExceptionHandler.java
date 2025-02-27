package ru.yandex.practicum.shop.controller;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.shop.exception.AlreadyExistsInCartException;
import ru.yandex.practicum.shop.exception.NoItemInCartException;
import ru.yandex.practicum.shop.exception.NoProductsInOrderException;
import ru.yandex.practicum.shop.exception.ResourceNotFoundException;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({AlreadyExistsInCartException.class, NoProductsInOrderException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBadRequestException(Exception e, Model model) {
        model.addAttribute("error", Map.of(
                "code", HttpStatus.BAD_REQUEST,
                "text", e.getMessage()
        ));
        return "oops";
    }

    @ExceptionHandler({ResourceNotFoundException.class, NoItemInCartException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFoundException(Exception e, Model model) {
        model.addAttribute("error", Map.of(
                "code", HttpStatus.NOT_FOUND,
                "text", e.getMessage()
        ));
        return "oops";
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
