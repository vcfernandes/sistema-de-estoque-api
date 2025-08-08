package Aquino.Sistema_de_Estoque.Security;
import Aquino.Sistema_de_Estoque.Model.Usuario;
import Aquino.Sistema_de_Estoque.Repository.UsuarioRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true) // Mantenha esta anotação, ela é importante
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Usuario usuario = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o nome: " + username));

    Set<GrantedAuthority> authorities = usuario
            .getRoles()
            .stream()
            .map(role -> new SimpleGrantedAuthority(role.getNome()))
            .collect(Collectors.toSet());

    // --- LOG DE DEBUG 1 ---
    System.out.println(">>> CustomUserDetailsService: Carregando usuário '" + username + "' com papéis: " + authorities);
    // --------------------

    return new User(usuario.getUsername(), usuario.getPassword(), authorities);
}

    
}