package Aquino.Sistema_de_Estoque.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
        protected void doFilterInternal(@org.springframework.lang.NonNull HttpServletRequest request, 
                                       @org.springframework.lang.NonNull HttpServletResponse response, 
                                       @org.springframework.lang.NonNull FilterChain filterChain)
        throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // Verifica se o cabeçalho existe e começa com "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // Extrai o token
            username = jwtService.extractUsername(token);
        }

        // Se temos um username e o usuário ainda não está autenticado no contexto de segurança atual
         if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        System.out.println(">>> JwtAuthFilter: UserDetails carregado para " + username + " com papéis: " + userDetails.getAuthorities());
            // Valida o token
            if (jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    
    // Define a autenticação no contexto de segurança
    SecurityContextHolder.getContext().setAuthentication(authToken);
}
        }
        // Continua a cadeia de filtros
        filterChain.doFilter(request, response);
    }
}