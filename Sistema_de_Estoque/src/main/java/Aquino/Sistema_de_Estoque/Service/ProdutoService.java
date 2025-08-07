package Aquino.Sistema_de_Estoque.Service;

import Aquino.Sistema_de_Estoque.DTO.ProdutoDto;
import Aquino.Sistema_de_Estoque.Model.Produto;
import Aquino.Sistema_de_Estoque.Model.Usuario;
import Aquino.Sistema_de_Estoque.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final TransacaoRepository transacaoRepository;
    private final LocalizacaoRepository localizacaoRepository;

    public Produto criarProduto(ProdutoDto produtoDto, Usuario usuarioLogado) {
        if (produtoRepository.findByNomeAndUsuario(produtoDto.getNome(), usuarioLogado).isPresent()) {
            throw new IllegalStateException("Produto com o nome '" + produtoDto.getNome() + "' já existe para este usuário.");
        }
        Produto novoProduto = new Produto();
        novoProduto.setNome(produtoDto.getNome());
        novoProduto.setDescricao(produtoDto.getDescricao());
        novoProduto.setPreco(produtoDto.getPreco());
        novoProduto.setQuantidadeEmEstoque(0);
        novoProduto.setUsuario(usuarioLogado);
        return produtoRepository.save(novoProduto);
    }

    @Transactional
    public List<Produto> criarMultiplosProdutos(List<ProdutoDto> produtosDto, Usuario usuarioLogado) {
        List<Produto> novosProdutos = produtosDto.stream().map(dto -> {
            if (produtoRepository.findByNomeAndUsuario(dto.getNome(), usuarioLogado).isPresent()) {
                throw new IllegalStateException("Operação em lote falhou: Produto com o nome '" + dto.getNome() + "' já existe.");
            }
            Produto produto = new Produto();
            produto.setNome(dto.getNome());
            produto.setDescricao(dto.getDescricao());
            produto.setPreco(dto.getPreco());
            produto.setQuantidadeEmEstoque(0);
            produto.setUsuario(usuarioLogado);
            return produto;
        }).collect(Collectors.toList());
        return produtoRepository.saveAll(novosProdutos);
    }

    public List<Produto> listarProdutosDoUsuario(Usuario usuarioLogado) {
        if (isAdmin(usuarioLogado)) {
            return produtoRepository.findAllByOrderByIdAsc();
        } else {
            return produtoRepository.findAllByUsuarioOrderByIdAsc(usuarioLogado);
        }
    }

    public Produto buscarProdutoPorId(Long produtoId, Usuario usuarioLogado) {
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto com ID " + produtoId + " não encontrado."));
        if (!isAdmin(usuarioLogado) && !produto.getUsuario().getId().equals(usuarioLogado.getId())) {
            throw new SecurityException("Acesso negado para visualizar este produto.");
        }
        return produto;
    }

    @Transactional
    public void deletarProduto(Long produtoId, Usuario usuarioLogado) {
        Produto produto = this.buscarProdutoPorId(produtoId, usuarioLogado); // Reutiliza a lógica de busca e verificação de permissão

        if (transacaoRepository.existsByProdutoId(produtoId)) {
            throw new IllegalStateException("Não é possível deletar o produto pois ele possui um histórico de transações.");
        }
        localizacaoRepository.deleteByProdutoId(produtoId);
        produtoRepository.deleteById(produtoId);
    }

    // --- MÉTODO AUXILIAR CORRIGIDO ---
    // Este método não depende de nada externo, apenas do objeto Usuario
    private boolean isAdmin(Usuario usuario) {
        return usuario.getRoles().stream().anyMatch(role -> "ROLE_ADMIN".equals(role.getNome()));
    }
}