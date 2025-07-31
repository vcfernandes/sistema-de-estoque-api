package Aquino.Sistema_de_Estoque.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LocalizacaoDto {
    @NotNull private Long produtoId;
    @NotNull private Long prateleiraId;
    @NotBlank private String linha;
    @NotBlank private String coluna;
}