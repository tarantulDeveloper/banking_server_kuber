package kg.saimatelecom.catalog.repository;

import kg.saimatelecom.catalog.dto.response.MonthlyPurchaseResponseForManager;
import kg.saimatelecom.catalog.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase,Long> {

    @Query(value = "SELECT count(*) from purchases",nativeQuery = true)
    Integer getTotalPurchases();


    @Query("SELECT NEW kg.saimatelecom.catalog.dto.response.MonthlyPurchaseResponseForManager(" +
            "TRIM(to_char(p.createdAt, 'Month')), count(*)) " +
            "FROM Purchase p " +
            "WHERE YEAR(p.createdAt) = :year " +
            "GROUP BY TRIM(to_char(p.createdAt, 'Month'))")
    List<MonthlyPurchaseResponseForManager> getPurchaseTotalByMonth(@Param("year") int year);


}
