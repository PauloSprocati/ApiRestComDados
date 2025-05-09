package umfg.edu.comdados.restAPI.Config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import umfg.edu.comdados.restAPI.Services.MyUserDetailsService;
import umfg.edu.comdados.restAPI.Utils.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {
        // Extrai o header "Authorization"
        final String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // Verifica se o header está presente e começa com "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // remove o "Bearer "
            try {
                // Utilizamos o JwtUtil para extrair o username (pode ser o e-mail, conforme sua implementação)
                username = jwtUtil.extractUsername(token);
            } catch (Exception e) {
                // Aqui você pode logar o erro ou tratar expirations/token malformado
                logger.error("Erro ao extrair o username do token: " + e.getMessage());
            }
        }

        // Se o username foi extraído e o contexto de segurança ainda não possui autenticação
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Busca os detalhes do usuário (UserDetails) a partir do banco
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(token, userDetails)) {
                // Cria a autenticação e define no contexto do Spring Security
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        // Continua a cadeia de filtros
        chain.doFilter(request, response);
    }
}
