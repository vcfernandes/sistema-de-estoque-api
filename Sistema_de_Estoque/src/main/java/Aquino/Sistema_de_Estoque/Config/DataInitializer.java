package Aquino.Sistema_de_Estoque.Config;

import Aquino.Sistema_de_Estoque.Model.Role;
import Aquino.Sistema_de_Estoque.Model.Usuario;
import Aquino.Sistema_de_Estoque.Repository.RoleRepository;
import Aquino.Sistema_de_Estoque.Repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration // Diz ao Spring que esta classe contém beans de configuração
@RequiredArgsConstructor
public class DataInitializer {

    // Injetamos as dependências necessárias através do construtor
    private final RoleRepository roleRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initDatabase() {
        return args -> {
            // A lógica é exatamente a mesma de antes
            if (roleRepository.findByNome("ROLE_ADMIN").isEmpty()) {
                System.out.println(">>> Populando banco de dados com dados iniciais...");

                // --- Criar Papéis (Roles) ---
                Role adminRole = new Role();
                adminRole.setNome("ROLE_ADMIN");
                roleRepository.save(adminRole);

                Role userRole = new Role();
                userRole.setNome("ROLE_USER");
                roleRepository.save(userRole);
                
                // --- Criar Usuário Mestre (Admin) ---
                Usuario admin = new Usuario();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123")); // Use uma senha forte em produção!
                admin.setRoles(Set.of(adminRole));
                
                usuarioRepository.save(admin);
                
                System.out.println(">>> Papéis e Usuário Admin criados com sucesso! <<<");
            }
        };
    }
}