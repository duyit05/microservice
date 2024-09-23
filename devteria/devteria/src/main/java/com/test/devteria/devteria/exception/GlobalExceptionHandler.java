package com.test.devteria.devteria.exception;

import com.test.devteria.devteria.dto.response.ApiRespone;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    public static final ApiRespone respone = new ApiRespone();
    public static final String MIN_ATTRIBUTE = "min";

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiRespone> handlingRuntimeException(RuntimeException exception) {
        respone.setCode(1001);
        respone.setMessage(exception.getMessage());
        return ResponseEntity.badRequest().body(respone);
    }
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiRespone> handlingRuntimeException(AppException exception) {

        ErrorCode errorCode = exception.getErrorCode();
        respone.setCode(errorCode.getCode());
        respone.setMessage(exception.getMessage());
        return ResponseEntity.status(errorCode.getStatusCode()).body(respone);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ApiRespone> handlingAccessDeniedException(AccessDeniedException exception) {
        ErrorCode errorCode = ErrorCode.UNCATEGORIZED;
        return ResponseEntity.status(errorCode.getStatusCode())
                .body(ApiRespone.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiRespone> handlingValidation(MethodArgumentNotValidException exception) {
        String enumKey = exception.getFieldError().getDefaultMessage();

        ErrorCode errorCode = ErrorCode.KEY_INVALID;

        Map<String, Object> attributes = new HashMap<>();
        try {
            errorCode = ErrorCode.valueOf(enumKey);
            ConstraintViolation<?> constraintViolation =
                    exception.getBindingResult().getAllErrors().get(0).unwrap(ConstraintViolation.class);

            attributes = constraintViolation.getConstraintDescriptor().getAttributes();

            log.info(attributes.toString());
        } catch (IllegalArgumentException e) {

        }
        respone.setCode(errorCode.getCode());
        respone.setMessage( Objects.nonNull(attributes) ? mapAttribute(errorCode.getMessage(), attributes) : errorCode.getMessage());

        return ResponseEntity.badRequest().body(respone);
    }

    private String mapAttribute(String message, Map<String, Object> attributes) {
        // CHANGE OBJECT TO STRING
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));

        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
    }
}
