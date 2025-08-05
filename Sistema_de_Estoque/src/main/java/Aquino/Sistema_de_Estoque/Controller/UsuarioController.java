package Aquino.Sistema_de_Estoque.Controller;

import Aquino.Sistema_de_Estoque.DTO.UsuarioDto; // Criaremos este DTO
import Aquino.Sistema_de_Estoque.Model.Usuario;
import Aquino.Sistema_de_Estoque.Service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Importante para segurança
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    // Endpoint para o ADMIN criar um novo usuário
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // SÓ ADMIN PODE ACESSAR!
    public ResponseEntity<Usuario> criarUsuario(@Valid @RequestBody UsuarioDto dto) {
        Usuario novoUsuario = usuarioService.criarUsuarioComRole(dto); // Método novo no serviço
        return new ResponseEntity<>(novoUsuario, HttpStatus.CREATED);
    }

    // Endpoint para o ADMIN listar todos os usuários
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')") // SÓ ADMIN PODE ACESSAR!
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    // Futuramente: Endpoints para deletar e atualizar usuários, também protegidos com @PreAuthorize
}