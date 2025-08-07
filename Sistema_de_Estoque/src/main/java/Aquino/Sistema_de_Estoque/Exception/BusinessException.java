package Aquino.Sistema_de_Estoque.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) 
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}