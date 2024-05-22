package kg.saimatelecom.clientservice.repository;

import kg.saimatelecom.clientservice.model.User;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    @Query("SELECT * FROM users WHERE email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    @Query(value = "SELECT EXISTS(SELECT u.id FROM users u WHERE u.email = :email)")
    boolean existsByEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM users u WHERE u.activation_token = :token")
    Optional<User> findByActivationToken(@Param("token") String token);

}
