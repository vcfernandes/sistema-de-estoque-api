package Aquino.Sistema_de_Estoque.DTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PrateleiraDto {

    @NotBlank(message = "O código da prateleira não pode ser vazio.")
    private String codigo;
    

}