package Aquino.Sistema_de_Estoque.Controller;
import Aquino.Sistema_de_Estoque.DTO.ProdutoDto;
import Aquino.Sistema_de_Estoque.Model.Produto;
import Aquino.Sistema_de_Estoque.Model.Usuario;
import Aquino.Sistema_de_Estoque.Repository.UsuarioRepository;
import Aquino.Sistema_de_Estoque.Service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController // Anotação que combina @Controller e @ResponseBody. Diz ao Spring que esta classe é um controller REST.
@RequestMapping("/api/produtos") // Define o prefixo da URL para todos os endpoints nesta classe.
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService; 
    private final UsuarioRepository usuarioRepository;

    @GetMapping("/") // Mapeia para o caminho raiz
    public String home() {
    return "Bem-vindo à API do Sistema de Estoque! Acesse /api/produtos para ver os produtos.";
}
    // Endpoint para CRIAR um novo produto
    // POST http://localhost:5433/api/produtos
   
    @PostMapping
    public ResponseEntity<Produto> criarProduto(@Valid @RequestBody ProdutoDto produtoDto, Authentication authentication) {
    Usuario usuarioLogado = getUsuarioLogado(authentication);
    Produto novoProduto = produtoService.criarProduto(produtoDto, usuarioLogado); // Passa o usuário
    return new ResponseEntity<>(novoProduto, HttpStatus.CREATED);
}

    // Endpoint para LISTAR todos os produtos
    // GET http://localhost:5433/api/produtos
    @GetMapping
    public ResponseEntity<List<Produto>> listarProdutos(Authentication authentication) {
    Usuario usuarioLogado = getUsuarioLogado(authentication);
    List<Produto> produtos = produtoService.listarProdutosDoUsuario(usuarioLogado); // Passa o usuário
    return ResponseEntity.ok(produtos);
   }

    // Endpoint para BUSCAR um produto por ID
    // GET http://localhost:5433/api/produtos/1 (onde 1 é o ID)
   @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarProdutoPorId(@PathVariable Long id, Authentication authentication) {
        Usuario usuarioLogado = getUsuarioLogado(authentication);
        Produto produto = produtoService.buscarProdutoPorId(id, usuarioLogado);
        
        return ResponseEntity.ok(produto);
    }

     // POST http://localhost:8080/api/produtos/batch
   @PostMapping("/batch")
    public ResponseEntity<List<Produto>> criarMultiplosProdutos(@Valid @RequestBody List<ProdutoDto> produtosDto, Authentication authentication) {
    Usuario usuarioLogado = getUsuarioLogado(authentication);
    List<Produto> produtosCriados = produtoService.criarMultiplosProdutos(produtosDto, usuarioLogado); // Passa o usuário
    return new ResponseEntity<>(produtosCriados, HttpStatus.CREATED);
   }

     @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable Long id, Authentication authentication) {
        try {
            Usuario usuarioLogado = getUsuarioLogado(authentication);
            produtoService.deletarProduto(id, usuarioLogado);
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

   private Usuario getUsuarioLogado(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("Acesso não autorizado.");
        }
        String username = authentication.getName();
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
    }
    
}