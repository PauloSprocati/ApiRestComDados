package umfg.edu.comdados.restAPI.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import umfg.edu.comdados.restAPI.Entities.UserEntity;
import umfg.edu.comdados.restAPI.Repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserEntity cadastrar(UserEntity usuario) {
        // Criptografa novamente a senha antes de salvar
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    public UserEntity buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email).orElse(null);
    }
}
