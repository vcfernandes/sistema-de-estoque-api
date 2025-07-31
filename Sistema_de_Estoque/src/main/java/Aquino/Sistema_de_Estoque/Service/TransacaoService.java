package Aquino.Sistema_de_Estoque.Service;

import Aquino.Sistema_de_Estoque.DTO.EntradaSaidaDto;
import Aquino.Sistema_de_Estoque.Model.Produto;
import Aquino.Sistema_de_Estoque.Model.TipoTransacao;
import Aquino.Sistema_de_Estoque.Model.Transacao;
import Aquino.Sistema_de_Estoque.Repository.ProdutoRepository;
import Aquino.Sistema_de_Estoque.Repository.TransacaoRepository;
import jakarta.transaction.Transactional; // Importante: usar jakarta.transaction ou org.springframework.transaction
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor // Cria um construtor para injetar as dependências 'final'. É a melhor prática.
public class TransacaoService {

    // Injetando os repositórios que vamos precisar
    private final ProdutoRepository produtoRepository;
    private final TransacaoRepository transacaoRepository;

    @Transactional // Anotação CRUCIAL!
    public Transacao registrarEntrada(EntradaSaidaDto entradaDto) {
        // 1. Validar se o produto existe
        Produto produto = produtoRepository.findById(entradaDto.getProdutoId())
                .orElseThrow(() -> new RuntimeException("Erro: Produto com ID " + entradaDto.getProdutoId() + " não encontrado."));

        // 2. Atualizar a quantidade em estoque do produto
        int novaQuantidade = produto.getQuantidadeEmEstoque() + entradaDto.getQuantidade();
        produto.setQuantidadeEmEstoque(novaQuantidade);
        produtoRepository.save(produto); // Salva o produto com a nova quantidade

        // 3. Criar e salvar o registro da transação
        Transacao novaTransacao = new Transacao();
        novaTransacao.setProduto(produto);
        novaTransacao.setTipo(TipoTransacao.ENTRADA);
        novaTransacao.setQuantidade(entradaDto.getQuantidade());
        novaTransacao.setDataTransacao(LocalDateTime.now());

        return transacaoRepository.save(novaTransacao);
    }

    @Transactional
    public Transacao registrarSaida(EntradaSaidaDto saidaDto) {
        // 1. Validar se o produto existe
        Produto produto = produtoRepository.findById(saidaDto.getProdutoId())
                .orElseThrow(() -> new RuntimeException("Erro: Produto com ID " + saidaDto.getProdutoId() + " não encontrado."));

        // 2. Validar se há estoque suficiente (REGRA DE NEGÓCIO)
        if (produto.getQuantidadeEmEstoque() < saidaDto.getQuantidade()) {
            throw new RuntimeException("Estoque insuficiente para o produto: " + produto.getNome() +
                    ". Quantidade atual: " + produto.getQuantidadeEmEstoque() +
                    ", Tentativa de saída: " + saidaDto.getQuantidade());
        }

        // 3. Atualizar a quantidade em estoque
        int novaQuantidade = produto.getQuantidadeEmEstoque() - saidaDto.getQuantidade();
        produto.setQuantidadeEmEstoque(novaQuantidade);
        produtoRepository.save(produto);

        // 4. Criar e salvar o registro da transação
        Transacao novaTransacao = new Transacao();
        novaTransacao.setProduto(produto);
        novaTransacao.setTipo(TipoTransacao.SAIDA);
        novaTransacao.setQuantidade(saidaDto.getQuantidade());
        novaTransacao.setDataTransacao(LocalDateTime.now());

        return transacaoRepository.save(novaTransacao);
    }
}