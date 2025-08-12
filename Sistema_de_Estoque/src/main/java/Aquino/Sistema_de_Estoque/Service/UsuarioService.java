package Aquino.Sistema_de_Estoque.Service;
import Aquino.Sistema_de_Estoque.DTO.UsuarioDto;
import Aquino.Sistema_de_Estoque.Exception.BusinessException;
import Aquino.Sistema_de_Estoque.Exception.ResourceNotFoundException;
import Aquino.Sistema_de_Estoque.Model.Role;
import Aquino.Sistema_de_Estoque.Model.Usuario;
import Aquino.Sistema_de_Estoque.Repository.RoleRepository;
import Aquino.Sistema_de_Estoque.Repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.stream.Collectors;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.List;
import Aquino.Sistema_de_Estoque.DTO.UsuarioResponseDto; 

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

    @Transactional
    public UsuarioResponseDto atualizarUsuario(Long usuarioId, UsuarioDto usuarioDto) {
        Usuario usuarioParaAtualizar = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário com ID " + usuarioId + " não encontrado."));
        usuarioRepository.findByUsername(usuarioDto.getUsername())
            .ifPresent(usuarioEncontrado -> {
                if (!usuarioEncontrado.getId().equals(usuarioId)) {
                    throw new BusinessException("O username '" + usuarioDto.getUsername() + "' já está em uso por outro usuário.");
                }
            });

        usuarioParaAtualizar.setUsername(usuarioDto.getUsername());
        if (usuarioDto.getPassword() != null && !usuarioDto.getPassword().isEmpty()) {
            usuarioParaAtualizar.setPassword(passwordEncoder.encode(usuarioDto.getPassword()));
        }
        if (usuarioDto.getRoles() != null && !usuarioDto.getRoles().isEmpty()) {
            Set<Role> roles = usuarioDto.getRoles().stream()
                .map(roleName -> roleRepository.findByNome(roleName)
                        .orElseThrow(() -> new ResourceNotFoundException("Erro: Papel '" + roleName + "' não encontrado.")))
                .collect(Collectors.toSet());
        if (usuarioParaAtualizar.getUsername().equals("admin") && roles.stream().noneMatch(r -> r.getNome().equals("ROLE_ADMIN"))) {
                throw new BusinessException("Não é permitido remover o papel de ADMIN do usuário 'admin' principal.");
            }
            
            usuarioParaAtualizar.setRoles(roles);
        }

        Usuario usuarioSalvo = usuarioRepository.save(usuarioParaAtualizar);
        return toUsuarioResponseDto(usuarioSalvo); 
    }

     private UsuarioResponseDto toUsuarioResponseDto(Usuario usuario) {
        UsuarioResponseDto dto = new UsuarioResponseDto();
        dto.setId(usuario.getId());
        dto.setUsername(usuario.getUsername());

        // Mapeia o Set<Role> para um Set<String> com os nomes dos papéis
        Set<String> roleNames = usuario.getRoles().stream()
                .map(Role::getNome)
                .collect(Collectors.toSet());
        dto.setRoles(roleNames);

        return dto;
    }

     public List<UsuarioResponseDto> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::toUsuarioResponseDto) // Usa nosso novo método de conversão
                .collect(Collectors.toList());
    }
}