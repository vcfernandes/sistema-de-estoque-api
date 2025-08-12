package Aquino.Sistema_de_Estoque.Service;

import Aquino.Sistema_de_Estoque.DTO.LocalizacaoDto;
import Aquino.Sistema_de_Estoque.Exception.ResourceNotFoundException;
import Aquino.Sistema_de_Estoque.Model.*;
import Aquino.Sistema_de_Estoque.Repository.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocalizacaoService {

    private final LocalizacaoRepository localizacaoRepository;
    private final ProdutoRepository produtoRepository;
    private final PrateleiraRepository prateleiraRepository;

    public Localizacao criarLocalizacao(LocalizacaoDto dto, Usuario usuarioLogado) {
        // Busca o produto e a prateleira
        Produto produto = produtoRepository.findById(dto.getProdutoId())
                .orElseThrow(() -> new ResourceNotFoundException("Produto com ID " + dto.getProdutoId() + " não encontrado."));
        Prateleira prateleira = prateleiraRepository.findById(dto.getPrateleiraId())
                .orElseThrow(() -> new ResourceNotFoundException("Prateleira com ID " + dto.getPrateleiraId() + " não encontrada."));

        // *** VERIFICAÇÃO DE SEGURANÇA CRUCIAL ***
        // Garante que o usuário só pode criar localizações para seus próprios produtos e prateleiras.
        if (!produto.getUsuario().getId().equals(usuarioLogado.getId()) || 
            !prateleira.getUsuario().getId().equals(usuarioLogado.getId())) {
            throw new SecurityException("Acesso negado: O produto ou a prateleira não pertencem a você.");
        }

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

    public List<Localizacao> findByUsuario(Usuario usuarioLogado) {
    return localizacaoRepository.findAllByUsuario(usuarioLogado);
}
}