package ao.samid.file.handler;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.UUID;

@RestControllerAdvice
@Hidden
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomException(CustomException ex, HttpServletRequest request) {
        ex.printStackTrace();
        return ResponseEntity.status(ex.getCode())
                .body(ErrorResponse.builder()
                        .code(ex.getCode())
                        .message(ex.getMessage()+" bu ise her zaman custom exceptiondir")
                        .url(request.getRequestURL().toString())
                        .build());
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex,HttpServletRequest request) {
        ex.fillInStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.builder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message(ex.getMessage() + " \n Burada yazilan umumi exception")
                        .url(request.getRequestURI()) // URL is not available in this context
                        .build());
    }
}
