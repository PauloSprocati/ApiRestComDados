package umfg.edu.comdados.restAPI.Controllers;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import umfg.edu.comdados.restAPI.Entities.UserEntity;
import umfg.edu.comdados.restAPI.Services.LoginAttemptService;
import umfg.edu.comdados.restAPI.Services.UserService;
import umfg.edu.comdados.restAPI.Utils.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    // Para o endpoint /login usaremos o AuthenticationManager que precisará estar configurado
    @Autowired
    private AuthenticationManager authenticationManager;

    // Endpoint para cadastro
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserEntity usuario, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Retorna apenas a primeira mensagem de erro
            String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return ResponseEntity.badRequest().body(errorMessage);
        }

        UserEntity novoUsuario = userService.cadastrar(usuario);
        return ResponseEntity.ok(novoUsuario);
    }

    // Endpoint para login
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserEntity usuario, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            // Retorna apenas a primeira mensagem de erro
            String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return ResponseEntity.badRequest().body(errorMessage);
        }

        String key = usuario.getEmail(); // Usando o email como chave; você também pode usar o IP da requisição

        // Verifica se o usuário está bloqueado devido a muitas tentativas
        if (loginAttemptService.isBlocked(key)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Conta bloqueada temporariamente devido a muitas tentativas de login.");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usuario.getEmail(), usuario.getSenha())
            );
            // Se o login for bem-sucedido, limpe as tentativas anteriores
            loginAttemptService.loginSucceeded(key);
        } catch (AuthenticationException e) {
            // Se a autenticação falhar, registre a tentativa
            loginAttemptService.loginFailed(key);
            return ResponseEntity.badRequest().body("Credenciais inválidas.");
        }

        String token = jwtUtil.generateToken(usuario.getEmail());
        return ResponseEntity.ok(token);
    }
}