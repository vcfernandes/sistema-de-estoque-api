package Aquino.Sistema_de_Estoque.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data 
@AllArgsConstructor
public class LoginResponseDto {
    private String token;
}