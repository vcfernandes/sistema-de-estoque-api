package Aquino.Sistema_de_Estoque.Controller;
import Aquino.Sistema_de_Estoque.DTO.ProdutoDto;
import Aquino.Sistema_de_Estoque.Model.Produto;
import Aquino.Sistema_de_Estoque.Service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Anotação que combina @Controller e @ResponseBody. Diz ao Spring que esta classe é um controller REST.
@RequestMapping("/api/produtos") // Define o prefixo da URL para todos os endpoints nesta classe.
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService; // Injeta o nosso serviço de produtos.

    @GetMapping("/") // Mapeia para o caminho raiz
    public String home() {
    return "Bem-vindo à API do Sistema de Estoque! Acesse /api/produtos para ver os produtos.";
}
    // Endpoint para CRIAR um novo produto
    // POST http://localhost:5433/api/produtos
    @PostMapping
    public ResponseEntity<Produto> criarProduto(@Valid @RequestBody ProdutoDto produtoDto) {
        Produto novoProduto = produtoService.criarProduto(produtoDto);
        return new ResponseEntity<>(novoProduto, HttpStatus.CREATED); // Retorna 201 Created
    }

    // Endpoint para LISTAR todos os produtos
    // GET http://localhost:5433/api/produtos
    @GetMapping
    public ResponseEntity<List<Produto>> listarTodosProdutos() {
        List<Produto> produtos = produtoService.listarTodosProdutos();
        return ResponseEntity.ok(produtos); // Retorna 200 OK
    }

    // Endpoint para BUSCAR um produto por ID
    // GET http://localhost:5433/api/produtos/1 (onde 1 é o ID)
    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarProdutoPorId(@PathVariable Long id) {
        // O .map e .orElseThrow é uma forma funcional de lidar com o Optional retornado pelo serviço.
        return produtoService.buscarProdutoPorId(id)
                .map(produto -> ResponseEntity.ok(produto)) // Se o produto for encontrado, retorna 200 OK com o produto.
                .orElse(ResponseEntity.notFound().build()); // Se não, retorna 404 Not Found.
    }

     // POST http://localhost:8080/api/produtos/batch
    @PostMapping("/batch")
    public ResponseEntity<List<Produto>> criarMultiplosProdutos(@Valid @RequestBody List<ProdutoDto> produtosDto) {
        List<Produto> produtosCriados = produtoService.criarMultiplosProdutos(produtosDto);
        return new ResponseEntity<>(produtosCriados, HttpStatus.CREATED);
    }

     @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable Long id) {
        try {
            produtoService.deletarProduto(id);
            // Retorna 204 No Content, que é o padrão para uma deleção bem-sucedida.
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            // Se o serviço lançar a exceção de regra de negócio, retornamos um erro claro.
            // 409 Conflict é um bom status para "não posso fazer isso por causa do estado atual".
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (RuntimeException e) {
            // Para o caso de "produto não encontrado".
            return ResponseEntity.notFound().build();
        }
    }
    
}