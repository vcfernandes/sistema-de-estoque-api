package Aquino.Sistema_de_Estoque;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(
				info = @Info(
								title = "Sistema de Estoque API",
								version = "1.0",
								description = "API para gerenciamento de estoque")
								) //http://localhost:8080/swagger-ui.html
public class SistemaDeEstoqueApplication {

	public static void main(String[] args) {
		SpringApplication.run(SistemaDeEstoqueApplication.class, args);
	}

}
