package Aquino.Sistema_de_Estoque.DTO;

import lombok.Data;

@Data
public class LocalizacaoResponseDto {
    private Long id;
    private String linha;
    private String coluna;
    private ProdutoSimplesResponseDto produto;
    private PrateleiraSimplesResponseDto prateleira;
}