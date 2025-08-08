package Aquino.Sistema_de_Estoque.DTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PrateleiraDto {

    @Schema (description = "Código único da prateleira", example = "PRAT-001")
    @NotBlank(message = "O código da prateleira não pode ser vazio.")
    private String codigo;
    

}