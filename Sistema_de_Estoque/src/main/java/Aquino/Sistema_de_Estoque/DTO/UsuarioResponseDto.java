package Aquino.Sistema_de_Estoque.DTO;

import lombok.Data;
import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class UsuarioResponseDto {
 
    private Long id;
    
    @Schema(description = "Nome de usuário único", example = "usuario123")
    private String username;
    
    // Podemos ter um DTO simples para o Role também, para não expor a entidade inteira
    private Set<String> roles; 

}