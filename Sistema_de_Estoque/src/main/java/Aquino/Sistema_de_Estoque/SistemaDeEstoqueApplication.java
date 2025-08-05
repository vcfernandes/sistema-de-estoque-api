package Aquino.Sistema_de_Estoque;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import Aquino.Sistema_de_Estoque.Model.Role;
import Aquino.Sistema_de_Estoque.Model.Usuario;
import Aquino.Sistema_de_Estoque.Repository.RoleRepository;
import Aquino.Sistema_de_Estoque.Repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(
				info = @Info(
								title = "Sistema de Estoque API",
								version = "1.0",
								description = "API para gerenciamento de estoque")
								) // http://localhost:8080/swagger-ui.html
public class SistemaDeEstoqueApplication {

	public static void main(String[] args) {
		SpringApplication.run(SistemaDeEstoqueApplication.class, args);
	}

	@Bean
	CommandLineRunner run(RoleRepository roleRepository, UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			// Só executa se o papel de ADMIN não existir, para não duplicar dados
			if (roleRepository.findByNome("ROLE_ADMIN").isEmpty()) {
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
				admin.setRoles(Set.of(adminRole)); // Associa o papel de ADMIN ao usuário
				
				usuarioRepository.save(admin);
				
				System.out.println(">>> Papéis e Usuário Admin criados com sucesso! <<<");
			}
		};
	}

}
