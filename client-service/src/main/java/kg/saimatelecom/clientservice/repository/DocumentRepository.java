package kg.saimatelecom.clientservice.repository;

import kg.saimatelecom.clientservice.model.Document;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentRepository extends CrudRepository {
    @Query(value = "SELECT * FROM documents WHERE username = :username")
    Optional<Document> findByUsername(@Param("username") String username);
}
