package umfg.edu.comdados.restAPI.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umfg.edu.comdados.restAPI.Entities.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
}
