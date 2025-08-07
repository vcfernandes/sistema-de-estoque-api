package Aquino.Sistema_de_Estoque.Controller;

import Aquino.Sistema_de_Estoque.DTO.PrateleiraDto;
import Aquino.Sistema_de_Estoque.Model.Prateleira;
import Aquino.Sistema_de_Estoque.Model.Usuario;
import Aquino.Sistema_de_Estoque.Repository.PrateleiraRepository;
import Aquino.Sistema_de_Estoque.Repository.UsuarioRepository;
import Aquino.Sistema_de_Estoque.Service.PrateleiraService;
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

    @PostMapping
public ResponseEntity<Prateleira> criarPrateleira(@RequestBody PrateleiraDto dto, Authentication authentication) {
    Usuario usuarioLogado = getUsuarioLogado(authentication); // Use um método auxiliar
    Prateleira prateleiraSalva = prateleiraService.criarPrateleira(dto, usuarioLogado);
    return new ResponseEntity<>(prateleiraSalva, HttpStatus.CREATED);
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
    @GetMapping
    public ResponseEntity<List<Prateleira>> listarPrateleiras() {
        return ResponseEntity.ok(prateleiraRepository.findAll());
    }
}