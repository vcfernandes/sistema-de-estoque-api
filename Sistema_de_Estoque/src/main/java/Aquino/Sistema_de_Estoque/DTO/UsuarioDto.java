package Aquino.Sistema_de_Estoque.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class UsuarioDto {
    @NotBlank
    @Schema(description = "Nome de usuário único", example = "usuario123")
    private String username;

    @Schema(description = "Senha do usuário", example = "senha123")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    private String password;

    // O admin vai especificar quais papéis o novo usuário terá
    private Set<String> roles; // Ex: ["ROLE_USER"] ou ["ROLE_ADMIN", "ROLE_USER"]
}