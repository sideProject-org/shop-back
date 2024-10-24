package toy.shop.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import toy.shop.dto.Response;

public class ResponseBuilder {

    public static ResponseEntity<Response<?>> buildResponse(HttpStatus status, String message, Object data) {
        Response<?> response = Response.builder()
                .status(status.value())
                .message(message)
                .data(data)
                .build();
        return new ResponseEntity<>(response, status);
    }
}
