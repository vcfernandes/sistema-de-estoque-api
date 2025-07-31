package Aquino.Sistema_de_Estoque.Service;

import Aquino.Sistema_de_Estoque.DTO.LocalizacaoDto;
import Aquino.Sistema_de_Estoque.Model.*;
import Aquino.Sistema_de_Estoque.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocalizacaoService {

    private final LocalizacaoRepository localizacaoRepository;
    private final ProdutoRepository produtoRepository;
    private final PrateleiraRepository prateleiraRepository;

    public Localizacao criarLocalizacao(LocalizacaoDto dto) {
        Produto produto = produtoRepository.findById(dto.getProdutoId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        Prateleira prateleira = prateleiraRepository.findById(dto.getPrateleiraId())
                .orElseThrow(() -> new RuntimeException("Prateleira não encontrada"));

        Localizacao novaLocalizacao = new Localizacao();
        novaLocalizacao.setProduto(produto);
        novaLocalizacao.setPrateleira(prateleira);
        novaLocalizacao.setLinha(dto.getLinha().toUpperCase());
        novaLocalizacao.setColuna(dto.getColuna());

        return localizacaoRepository.save(novaLocalizacao);
    }

    public void deletarLocalizacao(Long localizacaoId) {
        if (!localizacaoRepository.existsById(localizacaoId)) {
            throw new RuntimeException("Localização não encontrada");
        }
        localizacaoRepository.deleteById(localizacaoId);
    }
}