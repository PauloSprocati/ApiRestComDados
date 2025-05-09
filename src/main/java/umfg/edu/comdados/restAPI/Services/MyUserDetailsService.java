package umfg.edu.comdados.restAPI.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import umfg.edu.comdados.restAPI.Entities.UserEntity;
import umfg.edu.comdados.restAPI.Repository.UserRepository;

import java.util.ArrayList;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity usuario = usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o email: " + username));

        // Cria um objeto User do Spring Security com as informações do usuário.
        // Aqui estamos passando uma lista vazia para authorities, mas você pode incluir roles, se necessário.
        return new org.springframework.security.core.userdetails.User(
                usuario.getEmail(),
                usuario.getSenha(),
                new ArrayList<>()
        );
    }
}
