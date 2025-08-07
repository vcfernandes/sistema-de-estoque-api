package Aquino.Sistema_de_Estoque.Exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

// A anotação @ControllerAdvice torna esta classe um componente global para tratar exceções.
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        Map<String, String> errorBody = Map.of("erro", ex.getMessage());
        return new ResponseEntity<>(errorBody, HttpStatus.NOT_FOUND); // Retorna 404
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, String>> handleBusinessException(BusinessException ex) {
        Map<String, String> errorBody = Map.of("erro", ex.getMessage());
        return new ResponseEntity<>(errorBody, HttpStatus.CONFLICT); // Retorna 409
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<Map<String, String>> handleSecurityException(SecurityException ex) {
        Map<String, String> errorBody = Map.of("erro", ex.getMessage());
        return new ResponseEntity<>(errorBody, HttpStatus.FORBIDDEN); // Retorna 403
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        ex.printStackTrace(); // Para debug
        
        Map<String, String> errorBody = Map.of("erro", "Ocorreu um erro inesperado no servidor. Tente novamente mais tarde.");
        return new ResponseEntity<>(errorBody, HttpStatus.INTERNAL_SERVER_ERROR); // Retorna 500
    }
}