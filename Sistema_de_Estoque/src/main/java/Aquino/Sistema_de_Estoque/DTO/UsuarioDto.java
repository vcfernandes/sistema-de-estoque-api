package Aquino.Sistema_de_Estoque.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.Set;

@Data
public class UsuarioDto {
    @NotBlank
    private String username;

    @NotBlank
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    private String password;

    // O admin vai especificar quais papéis o novo usuário terá
    private Set<String> roles; // Ex: ["ROLE_USER"] ou ["ROLE_ADMIN", "ROLE_USER"]
}