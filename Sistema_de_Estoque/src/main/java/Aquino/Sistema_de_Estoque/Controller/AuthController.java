package Aquino.Sistema_de_Estoque.Controller;

import Aquino.Sistema_de_Estoque.DTO.LoginRequestDto;
import Aquino.Sistema_de_Estoque.DTO.LoginResponseDto;
import Aquino.Sistema_de_Estoque.Security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor 
public class AuthController {

    // A injeção de dependência continua a mesma
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Operation(summary = "Autenticação de Usuário", description = "Endpoint para autenticar um usuário e gerar um token JWT.")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> authenticate(@RequestBody LoginRequestDto request) {
        // Autentica o usuário usando o gerenciador de autenticação do Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // Se a autenticação for bem-sucedida, gera o token
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);

        // Retorna o token na resposta
        return ResponseEntity.ok(new LoginResponseDto(token));
    }
}