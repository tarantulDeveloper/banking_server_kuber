package kg.saimatelecom.catalog.repository;

import kg.saimatelecom.catalog.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<Purchase,Long> {


    @Query(value = "SELECT pu " +
            "FROM Purchase pu " +
            "JOIN pu.username u " +
            "JOIN pu.product pr "+
            "WHERE u = :email "+
            "AND pu.deleted = false")
    List<Purchase> findAllExistingByOwnerEmail(@Param("email") String email);
}
