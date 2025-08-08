package Aquino.Sistema_de_Estoque.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LocalizacaoDto {
    @Schema(description = "ID do produto associado à localização", example = "1")
    @NotNull private Long produtoId;
    
    @Schema(description = "ID da prateleira onde o produto está localizado", example = "2")
    @NotNull private Long prateleiraId;

    @Schema(description = "Linha da localização na prateleira", example = "A")
    @NotBlank private String linha;

    @Schema(description = "Coluna da localização na prateleira", example = "1")
    @NotBlank private String coluna;
}