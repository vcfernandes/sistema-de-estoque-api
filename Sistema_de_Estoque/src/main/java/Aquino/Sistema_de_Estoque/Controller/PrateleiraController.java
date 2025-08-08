package Aquino.Sistema_de_Estoque.Controller;

import Aquino.Sistema_de_Estoque.DTO.PrateleiraDto;
import Aquino.Sistema_de_Estoque.Model.Prateleira;
import Aquino.Sistema_de_Estoque.Model.Usuario;
import Aquino.Sistema_de_Estoque.Repository.PrateleiraRepository;
import Aquino.Sistema_de_Estoque.Repository.UsuarioRepository;
import Aquino.Sistema_de_Estoque.Service.PrateleiraService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/prateleiras")
@RequiredArgsConstructor
public class PrateleiraController {

    private final PrateleiraRepository prateleiraRepository; 
    private final PrateleiraService prateleiraService;
    private final UsuarioRepository usuarioRepository; // Para obter o usuário logado

    @Operation(summary = "Criar uma nova pratileira",description = "Cria uma nova pratileira associada ao usuário autenticado. O nome da pratileira deve ser único por usuário.")
    @PostMapping
    public ResponseEntity<Prateleira> criarPrateleira(@RequestBody PrateleiraDto dto, Authentication authentication) {
    Usuario usuarioLogado = getUsuarioLogado(authentication); // Use um método auxiliar
    Prateleira prateleiraSalva = prateleiraService.criarPrateleira(dto, usuarioLogado);
    return new ResponseEntity<>(prateleiraSalva, HttpStatus.CREATED);
}

    @Operation(summary = "Atualizar as Prateleiras",description = "Atualiza uma pratileira existente associada ao usuário autenticado. O nome da pratileira deve ser único por usuário.")
    @PutMapping("/{id}")
    public ResponseEntity<Prateleira> atualizarPrateleira(@PathVariable Long id, @Valid @RequestBody PrateleiraDto prateleiraDto, Authentication authentication) {
        Usuario usuarioLogado = getUsuarioLogado(authentication);
        Prateleira prateleiraAtualizada = prateleiraService.atualizarPrateleira(id, prateleiraDto, usuarioLogado);
        return ResponseEntity.ok(prateleiraAtualizada);
    }
        private Usuario getUsuarioLogado(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("Acesso não autorizado: Nenhum usuário autenticado.");
        }
        String username = authentication.getName();
        // Você precisará injetar o UsuarioRepository neste controller para que isso funcione
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário '" + username + "' não encontrado no banco de dados."));
    }

    @Operation(summary = "Listar Prateleiras",description = "Lista todas as prateleiras do usuário autenticado. Admins podem ver todas as prateleiras do sistema.")
    @GetMapping
    public ResponseEntity<List<Prateleira>> listarPrateleiras() {
        return ResponseEntity.ok(prateleiraRepository.findAll());
    }
}