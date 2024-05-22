package kg.saimatelecom.clientservice.repository;

import kg.saimatelecom.clientservice.model.ClientAccount;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ClientAccountRepository extends CrudRepository<ClientAccount, Long> {

    @Query(value = "SELECT * FROM client_accounts WHERE email = :email")
    Optional<ClientAccount> findByEmail(@Param("email") String email);

    @Query(value = "SELECT c.* FROM client_accounts c JOIN users u ON c.user_id = u.id WHERE u.email = ? LIMIT 1")
    Optional<ClientAccount> findClientAccountByUserEmail(@Param("email") String email);
}
