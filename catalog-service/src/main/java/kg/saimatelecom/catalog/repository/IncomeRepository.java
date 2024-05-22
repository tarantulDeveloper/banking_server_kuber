package kg.saimatelecom.catalog.repository;


import kg.saimatelecom.catalog.dto.response.MonthlyIncomeResponseForManager;
import kg.saimatelecom.catalog.model.Income;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface IncomeRepository extends CrudRepository<Income,Long> {


    @Query(value = "SELECT sum(i.income_amount) FROM incomes i where i.owner_type = 'SYSTEM'",nativeQuery = true)
    BigDecimal getSystemIncomesSum();

    @Query("SELECT NEW kg.saimatelecom.catalog.dto.response.MonthlyIncomeResponseForManager(" +
            "TRIM(to_char(i.createdAt, 'Month')), SUM(i.incomeAmount)) " +
            "FROM Income i " +
            "WHERE YEAR(i.createdAt) = :year AND i.ownerType='SYSTEM'" +
            "GROUP BY TRIM(to_char(i.createdAt, 'Month'))")
    List<MonthlyIncomeResponseForManager> getIncomeSumByMonth(@Param("year") int year);
}