package Aquino.Sistema_de_Estoque.Controller;
import Aquino.Sistema_de_Estoque.DTO.ProdutoDto;
import Aquino.Sistema_de_Estoque.Model.Produto;
import Aquino.Sistema_de_Estoque.Model.Usuario;
import Aquino.Sistema_de_Estoque.Repository.UsuarioRepository;
import Aquino.Sistema_de_Estoque.Service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Produtos", description = "Endpoints para gerenciamento de produtos") 
public class ProdutoController {

    private final ProdutoService produtoService; 
    private final UsuarioRepository usuarioRepository;

    @GetMapping("/") // Mapeia para o caminho raiz
    public String home() {
    return "Bem-vindo à API do Sistema de Estoque! Acesse /api/produtos para ver os produtos.";
}
    // Endpoint para CRIAR um novo produto
    // POST http://localhost:5433/api/produtos
   
    @Operation(summary = "Cria um novo produto",description = "Cria um novo produto associado ao usuário autenticado. O nome do produto deve ser único por usuário.")
    @PostMapping
    public ResponseEntity<Produto> criarProduto(@Valid @RequestBody ProdutoDto produtoDto, Authentication authentication) {
    Usuario usuarioLogado = getUsuarioLogado(authentication);
    Produto novoProduto = produtoService.criarProduto(produtoDto, usuarioLogado); // Passa o usuário
    return new ResponseEntity<>(novoProduto, HttpStatus.CREATED);
}

    // Endpoint para LISTAR todos os produtos
    // GET http://localhost:5433/api/produtos
    @Operation(summary = "Lista os produtos do usuário",description = "Retorna uma lista de todos os produtos pertencentes ao usuário autenticado. Admins podem ver todos os produtos do sistema.")
    @GetMapping
    public ResponseEntity<List<Produto>> listarProdutos(Authentication authentication) {
    Usuario usuarioLogado = getUsuarioLogado(authentication);
    List<Produto> produtos = produtoService.listarProdutosDoUsuario(usuarioLogado); // Passa o usuário
    return ResponseEntity.ok(produtos);
   }

    // Endpoint para BUSCAR um produto por ID
    // GET http://localhost:5433/api/produtos/1 (onde 1 é o ID)
   @Operation(summary = "Buscar Produto pro ID",description = "Depois da /produto, coloque o id do produto que deseja buscar. Retorna o produto se encontrado, ou 404 se não existir.")
   @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarProdutoPorId(@PathVariable Long id, Authentication authentication) {
        Usuario usuarioLogado = getUsuarioLogado(authentication);
        Produto produto = produtoService.buscarProdutoPorId(id, usuarioLogado);
        
        return ResponseEntity.ok(produto);
    }

     // POST http://localhost:8080/api/produtos/batch
   @Operation(summary = "Cria um novos produtos em massa ",description = "Cria um novos produtos associado ao usuário autenticado. O nome dos produtos deve ser único por usuário.")
   @PostMapping("/batch")
    public ResponseEntity<List<Produto>> criarMultiplosProdutos(@Valid @RequestBody List<ProdutoDto> produtosDto, Authentication authentication) {
    Usuario usuarioLogado = getUsuarioLogado(authentication);
    List<Produto> produtosCriados = produtoService.criarMultiplosProdutos(produtosDto, usuarioLogado); // Passa o usuário
    return new ResponseEntity<>(produtosCriados, HttpStatus.CREATED);
   }

   @PutMapping("/{id}")
   @Operation(summary = "Atualizar um novo produto",description = "Atualiza um produto existente associado ao usuário autenticado. O nome do produto deve ser único por usuário.")
    public ResponseEntity<Produto> atualizarProduto(@PathVariable Long id, @Valid @RequestBody ProdutoDto produtoDto, Authentication authentication) {
        Usuario usuarioLogado = getUsuarioLogado(authentication);
        Produto produtoAtualizado = produtoService.atualizarProduto(id, produtoDto, usuarioLogado);
        return ResponseEntity.ok(produtoAtualizado); // Retorna 200 OK com o objeto atualizado
    }
    @Operation(summary = "Deletar um produto",description = "Deleta um produto existente associado ao usuário autenticado. Retorna 204 No Content se a deleção for bem-sucedida, ou 404 se o produto não existir.")
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