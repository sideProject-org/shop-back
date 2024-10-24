package toy.shop.cmmn.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import toy.shop.dto.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ExceptionController {

    /**
     * 파라미터 유효성 검사
     * @param ex BindException
     * @return
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<Response<?>> handleBindException(BindException ex) {
        List<FieldError> errors = ex.getBindingResult().getFieldErrors();
        List errorList = new ArrayList<>();

        for (FieldError fieldError : errors) {
            Map err = new HashMap();
            err.put("fieldName", fieldError.getField());
            err.put("message", fieldError.getDefaultMessage());
            errorList.add(err);
        }

        Response<Object> response = Response.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message("유효성 검증 오류")
                .data(errorList)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * 이미 데이터가 존재함
     * @param ex ConflictException
     * @return
     */
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Response<?>> handleConflictException(ConflictException ex) {
        Response<?> response = Response.builder()
                .status(HttpStatus.CONFLICT.value())
                .message(ex.getMessage())
                .data(null)
                .build();

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    /**
     * 데이터가 존재하지 않음
     * @param ex
     * @return
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Response<?>> handleNotFoundException(NotFoundException ex) {
        Response<?> response = Response.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .message(ex.getMessage())
                .data(null)
                .build();

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    /**
     * 참조 중인 데이터가 존재함
     * @param ex
     * @return
     */
    @ExceptionHandler(DependentDataException.class)
    public ResponseEntity<Response<?>> handleDependentDataException(DependentDataException ex) {
        Response<?> response = Response.builder()
                .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .message(ex.getMessage())
                .data(null)
                .build();

        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /**
     * 유저 정보가 존재하지 않음
     * @param ex
     * @return
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Response<?>> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        Response<?> response = Response.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .data(null)
                .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * 인증이 실패한 경우
     * @param ex
     * @return
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Response<?>> handleBadCredentialsException(BadCredentialsException ex) {
        Response<?> response = Response.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .message(ex.getMessage())
                .data(null)
                .build();

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
}
