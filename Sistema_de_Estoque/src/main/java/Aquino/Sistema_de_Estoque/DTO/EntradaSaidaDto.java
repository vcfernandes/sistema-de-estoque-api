package Aquino.Sistema_de_Estoque.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data // Anotação do Lombok que gera Getters, Setters, toString, etc.
public class EntradaSaidaDto {

    @NotNull(message = "O ID do produto não pode ser nulo.")
    private Long produtoId;

    @NotNull(message = "A quantidade não pode ser nula.")
    @Positive(message = "A quantidade deve ser um número positivo.")
    private Integer quantidade; // Usar Integer é comum para quantidades. int também funciona.

}
