package umfg.edu.comdados.restAPI.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import umfg.edu.comdados.restAPI.Entities.UserEntity;
import umfg.edu.comdados.restAPI.Services.UserService;
import umfg.edu.comdados.restAPI.Utils.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    // Para o endpoint /login usaremos o AuthenticationManager que precisará estar configurado
    @Autowired
    private AuthenticationManager authenticationManager;

    // Endpoint para cadastro
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserEntity usuario) {
        // Idealmente, você deve validar os dados recebidos
        UserEntity novoUsuario = userService.cadastrar(usuario);
        return ResponseEntity.ok(novoUsuario);
    }

    // Endpoint para login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserEntity usuario) {
        try {
            // Tenta autenticar com base no email e senha enviados
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usuario.getEmail(), usuario.getSenha())
            );
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body("Credenciais inválidas.");
        }

        // Se autenticado, gera o token JWT
        String token = jwtUtil.generateToken(usuario.getEmail());
        return ResponseEntity.ok(token);
    }
}