package Aquino.Sistema_de_Estoque.Config;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration 

// 1. Define as informações gerais da API
@OpenAPIDefinition(
    info = @Info(
        title = "Sistema de Estoque API",
        version = "1.0",
        description = "API para gerenciamento completo de um sistema de estoque, incluindo produtos, transações e usuários."
    ),
    security = {
        @SecurityRequirement(name = "bearerAuth")
    }
)
@SecurityScheme(
    name = "bearerAuth", 
    description = "Token JWT de autenticação",
    scheme = "bearer", 
    type = SecuritySchemeType.HTTP, 
    bearerFormat = "JWT", 
    in = SecuritySchemeIn.HEADER 
)
public class SwaggerConfig {
    
}