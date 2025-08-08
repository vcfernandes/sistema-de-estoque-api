package Aquino.Sistema_de_Estoque.Controller;

import Aquino.Sistema_de_Estoque.DTO.UsuarioDto; // Criaremos este DTO
import Aquino.Sistema_de_Estoque.DTO.UsuarioResponseDto;
import Aquino.Sistema_de_Estoque.Model.Role;
import Aquino.Sistema_de_Estoque.Model.Usuario;
import Aquino.Sistema_de_Estoque.Service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

   @Operation(summary = "Criar um novo Usuário",description = "Cria um novo usuário com o papel de ROLE_USER. Admins podem criar usuários com papéis específicos.")
   @PostMapping
    // @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UsuarioResponseDto> criarUsuario(@Valid @RequestBody UsuarioDto dto) {
        Usuario novoUsuario = usuarioService.criarUsuarioComRole(dto);
        
        // Converte a entidade para o DTO de resposta aqui
        UsuarioResponseDto responseDto = new UsuarioResponseDto();
        responseDto.setId(novoUsuario.getId());
        responseDto.setUsername(novoUsuario.getUsername());
        responseDto.setRoles(novoUsuario.getRoles().stream().map(Role::getNome).collect(Collectors.toSet()));

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
    @Operation(summary = "Listar Usuários",description = "Lista todos os usuários do sistema. Admins podem ver todos os usuários, enquanto usuários comuns veem apenas seus próprios dados.")
    @GetMapping
    // @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<UsuarioResponseDto>> listarUsuarios() {
    List<UsuarioResponseDto> usuarios = usuarioService.listarTodos();
    return ResponseEntity.ok(usuarios);
}

    @Operation(summary = "Atualiza um Usuário",description = "Atualiza os dados de um usuário existente. Admins podem atualizar qualquer usuário, enquanto usuários comuns só podem atualizar seus próprios dados.")
    @PutMapping("/{id}")
    // @PreAuthorize("hasAuthority('ROLE_ADMIN')") 
    public ResponseEntity<UsuarioResponseDto> atualizarUsuario(@PathVariable Long id,@Valid @RequestBody UsuarioDto usuarioDto) {
    // --- LOG DE DEBUG DEFINITIVO ---
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    System.out.println(">>> DENTRO DO MÉTODO ATUALIZAR USUÁRIO <<<");
    System.out.println("Principal: " + authentication.getPrincipal());
    System.out.println("Autoridades: " + authentication.getAuthorities());
    // ---------------------------------

    UsuarioResponseDto usuarioAtualizado = usuarioService.atualizarUsuario(id, usuarioDto);
    return ResponseEntity.ok(usuarioAtualizado);
}

    
}