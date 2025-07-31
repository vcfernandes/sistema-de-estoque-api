package Aquino.Sistema_de_Estoque.Service;

import Aquino.Sistema_de_Estoque.DTO.ProdutoDto;
import Aquino.Sistema_de_Estoque.Model.Produto;
import Aquino.Sistema_de_Estoque.Repository.LocalizacaoRepository;
import Aquino.Sistema_de_Estoque.Repository.ProdutoRepository;
import Aquino.Sistema_de_Estoque.Repository.TransacaoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final TransacaoRepository transacaoRepository;// Injete para verificar o histórico
    private final LocalizacaoRepository localizacaoRepository;

    public Produto criarProduto(ProdutoDto produtoDto) {
        // Validação básica para evitar produtos duplicados pelo nome
        // Em uma aplicação real, você pode querer uma exceção mais específica
        if (produtoRepository.findByNome(produtoDto.getNome()).isPresent()) {
            throw new RuntimeException("Produto com o nome '" + produtoDto.getNome() + "' já existe.");
        }

        Produto novoProduto = new Produto();
        novoProduto.setNome(produtoDto.getNome());
        novoProduto.setDescricao(produtoDto.getDescricao());
        novoProduto.setPreco(produtoDto.getPreco());
        novoProduto.setQuantidadeEmEstoque(0); // Um produto sempre começa com 0 em estoque.

        return produtoRepository.save(novoProduto);
    }
    
     @Transactional
    public void deletarProduto(Long produtoId) {
        if (!produtoRepository.existsById(produtoId)) {
            throw new RuntimeException("Produto com ID " + produtoId + " não encontrado.");
        } 
        if (transacaoRepository.existsByProdutoId(produtoId)) {
            throw new IllegalStateException("Não é possível deletar o produto com ID " + produtoId + " porque ele possui um histórico de transações.");
        }
         localizacaoRepository.deleteAll(localizacaoRepository.findByProdutoId(produtoId));

         produtoRepository.deleteById(produtoId);
    }

     @Transactional // Garante que ou todos os produtos são salvos, ou nenhum é.
    public List<Produto> criarMultiplosProdutos(List<ProdutoDto> produtosDto) {
        // 1. Converte a lista de DTOs para uma lista de Entidades Produto
        List<Produto> novosProdutos = produtosDto.stream().map(dto -> {
            if (produtoRepository.findByNome(dto.getNome()).isPresent()) {
                throw new RuntimeException("Produto com o nome '" + dto.getNome() + "' já existe.");
            }

            Produto produto = new Produto();
            produto.setNome(dto.getNome());
            produto.setDescricao(dto.getDescricao());
            produto.setPreco(dto.getPreco());
            produto.setQuantidadeEmEstoque(0);
            return produto;
        }).collect(Collectors.toList());

        // 2. Salva todos os novos produtos no banco de uma só vez
        return produtoRepository.saveAll(novosProdutos);
    }

    public List<Produto> listarTodosProdutos() {
        return produtoRepository.findAllByOrderByIdAsc();
    }

    public Optional<Produto> buscarProdutoPorId(Long id) {
        return produtoRepository.findById(id);
    }

    

    // Para a validação acima funcionar, precisamos adicionar um método no nosso repositório.
    // Vá para ProdutoRepository.java e adicione o método abaixo.
}