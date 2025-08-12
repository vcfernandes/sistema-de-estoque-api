package Aquino.Sistema_de_Estoque.Mapper;

import org.springframework.stereotype.Component;

import Aquino.Sistema_de_Estoque.DTO.LocalizacaoResponseDto;
import Aquino.Sistema_de_Estoque.DTO.PrateleiraSimplesResponseDto;
import Aquino.Sistema_de_Estoque.DTO.ProdutoSimplesResponseDto;
import Aquino.Sistema_de_Estoque.Model.Localizacao;

@Component
public class LocalizacaoMapper {
    public LocalizacaoResponseDto toDto(Localizacao localizacao) {
        ProdutoSimplesResponseDto produtoDto = new ProdutoSimplesResponseDto();
        produtoDto.setId(localizacao.getProduto().getId());
        produtoDto.setNome(localizacao.getProduto().getNome());

        PrateleiraSimplesResponseDto prateleiraDto = new PrateleiraSimplesResponseDto();
        prateleiraDto.setId(localizacao.getPrateleira().getId());
        prateleiraDto.setCodigo(localizacao.getPrateleira().getCodigo());
        
        LocalizacaoResponseDto dto = new LocalizacaoResponseDto();
        dto.setId(localizacao.getId());
        dto.setLinha(localizacao.getLinha());
        dto.setColuna(localizacao.getColuna());
        dto.setProduto(produtoDto);
        dto.setPrateleira(prateleiraDto);
        
        return dto;
    }
}