package com.livevox.challenge.app.response;

import java.util.HashMap;

import com.livevox.challenge.app.response.exceptions.BadRequestException;
import com.livevox.challenge.app.response.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionResolver {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public HashMap<String, String> handleNoHandlerFound(final NotFoundException e) {
        return getBodyFromMessage(e.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public HashMap<String, String> handleNoHandlerFound(final BadRequestException e) {
        return getBodyFromMessage(e.getMessage());
    }

    private HashMap<String, String> getBodyFromMessage(final String message) {
        final HashMap<String, String> response = new HashMap<>();
        response.put("error", message);
        return response;
    }
}
