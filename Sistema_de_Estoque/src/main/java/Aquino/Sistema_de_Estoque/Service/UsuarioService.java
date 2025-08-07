package Aquino.Sistema_de_Estoque.Service;
import Aquino.Sistema_de_Estoque.DTO.UsuarioDto;
import Aquino.Sistema_de_Estoque.Exception.BusinessException;
import Aquino.Sistema_de_Estoque.Exception.ResourceNotFoundException;
import Aquino.Sistema_de_Estoque.Model.Role;
import Aquino.Sistema_de_Estoque.Model.Usuario;
import Aquino.Sistema_de_Estoque.Repository.RoleRepository;
import Aquino.Sistema_de_Estoque.Repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository; // Injete o repositório de papéis
    private final PasswordEncoder passwordEncoder;

    public Usuario criarUsuarioComRole(UsuarioDto dto) {
        if (usuarioRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new BusinessException("Username '" + dto.getUsername() + "' já está em uso.");
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setUsername(dto.getUsername());
        novoUsuario.setPassword(passwordEncoder.encode(dto.getPassword()));

        // Converte os nomes dos papéis (Strings) para entidades Role
        Set<Role> roles = dto.getRoles().stream()
                .map(roleName -> roleRepository.findByNome(roleName)
                        .orElseThrow(() -> new ResourceNotFoundException("Erro: Papel '" + roleName + "' não encontrado.")))
                .collect(Collectors.toSet());
        
        novoUsuario.setRoles(roles);
        
        return usuarioRepository.save(novoUsuario);
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }
}