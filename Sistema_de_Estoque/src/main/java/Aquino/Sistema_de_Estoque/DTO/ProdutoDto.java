package Aquino.Sistema_de_Estoque.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.math.BigDecimal;

@Data // DTOs também se beneficiam muito do Lombok
public class ProdutoDto {

    // Não precisamos do ID aqui, pois ele é gerado pelo banco.
    // Este DTO é para criar um novo produto.

    @NotBlank(message = "O nome do produto não pode ser vazio ou nulo.")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
    private String nome;

    private String descricao; // Descrição pode ser opcional

    @NotNull(message = "O preço não pode ser nulo.")
    @Positive(message = "O preço deve ser um valor positivo.")
    private BigDecimal preco;

    // A quantidade inicial pode ser zero, então não precisa de validação aqui.
    // Ela será controlada pela lógica de transação.
}
