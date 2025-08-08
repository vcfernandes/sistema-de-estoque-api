package Aquino.Sistema_de_Estoque.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
@Data
public class LoginRequestDto {
    
    @Schema(example = "admin")
    private String username;

    @Schema(example = "admin123")
    private String password;
}