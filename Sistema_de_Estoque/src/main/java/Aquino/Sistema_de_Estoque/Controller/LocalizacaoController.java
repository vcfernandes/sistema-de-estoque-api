package Aquino.Sistema_de_Estoque.Controller;

import Aquino.Sistema_de_Estoque.DTO.LocalizacaoDto;
import Aquino.Sistema_de_Estoque.DTO.LocalizacaoResponseDto;
import Aquino.Sistema_de_Estoque.Mapper.LocalizacaoMapper;
import Aquino.Sistema_de_Estoque.Model.Localizacao;
import Aquino.Sistema_de_Estoque.Model.Usuario;
import Aquino.Sistema_de_Estoque.Repository.LocalizacaoRepository;
import Aquino.Sistema_de_Estoque.Repository.UsuarioRepository;
import Aquino.Sistema_de_Estoque.Service.LocalizacaoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/localizacoes")
@RequiredArgsConstructor
public class LocalizacaoController {

    private final LocalizacaoService localizacaoService;
    private final LocalizacaoRepository localizacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final LocalizacaoMapper localizacaoMapper;

    // Endpoint para ALOCAR um produto em uma localização
    // POST http://localhost:8080/api/localizacoes
    @Operation(summary = "Criar uma nova localização",description = "Cria uma nova localização associada ao usuário autenticado. O nome da localização deve ser único por usuário.")
    @PostMapping
    public ResponseEntity<Localizacao> criarLocalizacao(@Valid @RequestBody LocalizacaoDto dto, Authentication authentication) {
    Usuario usuarioLogado = getUsuarioLogado(authentication);
    Localizacao novaLocalizacao = localizacaoService.criarLocalizacao(dto, usuarioLogado);
    return new ResponseEntity<>(novaLocalizacao, HttpStatus.CREATED);
}

    // Endpoint para BUSCAR todas as localizações de um produto
    // GET http://localhost:8080/api/localizacoes/produto/1
    @Operation(summary = "Buscar Localizações por Produto",description = "Busca todas as localizações associadas a um produto específico. Retorna uma lista de localizações.")
    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<List<Localizacao>> getLocalizacoesPorProduto(@PathVariable Long produtoId) {
        return ResponseEntity.ok(localizacaoRepository.findByProdutoId(produtoId));
    }

    // Endpoint para REMOVER uma alocação
    // DELETE http://localhost:8080/api/localizacoes/15 (onde 15 é o ID da localização)
    @Operation(summary = "Deletar uma localização",description = "Deleta uma localização existente associada ao usuário autenticado. Retorna 204 No Content se a deleção for bem-sucedida, ou 404 se a localização não existir.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarLocalizacao(@PathVariable Long id) {
        localizacaoService.deletarLocalizacao(id);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content
    }

// Endpoint para buscar localizações do usuário autenticado, retornando DTOs
    @GetMapping
    public ResponseEntity<List<LocalizacaoResponseDto>> getLocalizacoesDoUsuario(Authentication authentication) {
    Usuario usuarioLogado = getUsuarioLogado(authentication);
    List<Localizacao> localizacoes = localizacaoService.findByUsuario(usuarioLogado);

    // Converte a lista de entidades para uma lista de DTOs
    List<LocalizacaoResponseDto> dtos = localizacoes.stream()
        .map(localizacaoMapper::toDto)
        .collect(Collectors.toList());

    return ResponseEntity.ok(dtos);
  }
     private Usuario getUsuarioLogado(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("Acesso não autorizado: Nenhum usuário autenticado.");
        }
        String username = authentication.getName();
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário '" + username + "' não encontrado no banco de dados."));
    }
}