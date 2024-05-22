package kg.saimatelecom.catalog.service.impl;

import kg.saimatelecom.catalog.dto.response.IncomeResponseForAdmin;
import kg.saimatelecom.catalog.dto.response.IncomeResponseForDealer;
import kg.saimatelecom.catalog.dto.response.MonthlyIncomeResponseForManager;
import kg.saimatelecom.catalog.model.Income;
import kg.saimatelecom.catalog.repository.IncomeRepository;
import kg.saimatelecom.catalog.service.IncomeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IncomeServiceImpl implements IncomeService {

    IncomeRepository incomeRepository;

    @NonFinal
    @Value("${spring.datasource.url}")
    String dataSourceUrl;

    @NonFinal
    @Value("${spring.datasource.username}")
    String dataSourceUsername;

    @NonFinal
    @Value("${spring.datasource.password}")
    String dataSourcePassword;

    @Override
    public Income save(Income income) {
        return incomeRepository.save(income);
    }

    @Override
    public BigDecimal getIncomesSum() {
        return incomeRepository.getSystemIncomesSum();
    }

    @Override
    public List<MonthlyIncomeResponseForManager> getSystemMonthlyIncome() {
        return incomeRepository.getIncomeSumByMonth(Year.now().getValue());
    }

    @Override
    public List<MonthlyIncomeResponseForManager> getSystemMonthlyIncomeByYear(int year) {
        return incomeRepository.getIncomeSumByMonth(year);

    }

    @Override
    public List<IncomeResponseForDealer> getDealerIncomes(LocalDate startDate, LocalDate endDate, List<String> products, String email) {
        String sql = "SELECT i.id, i.created_at, i.income_amount, " +
                "p.percentage_of_profit_for_system, p.percentage_of_profit_for_dealer, " +
                "pr.cost, pr.real_cost, p.id AS product_id, p.name AS product_name " +
                "FROM users u " +
                "JOIN purchases pr ON u.id = pr.dealer_id " +
                "JOIN products p ON pr.product_id = p.id " +
                "JOIN incomes i ON pr.id = i.purchase_id " +
                "WHERE i.owner_type = 'DEALER'";



        List<Object> params = new ArrayList<>();

        sql+="AND u.email = ? ";
        params.add(email);

        if (startDate != null && endDate != null) {
            sql += "AND i.created_at BETWEEN ? AND ? ";
            params.add(startDate);
            params.add(endDate);
        } else if (startDate != null) {
            sql += "AND i.created_at >= ? ";
            params.add(startDate);
        } else if (endDate != null) {
            sql += "AND i.created_at <= ? ";
            params.add(endDate);
        }

        if (products != null && !products.isEmpty()) {
            sql += "AND p.name IN (" + String.join(",", Collections.nCopies(products.size(), "?")) + ")";
            params.addAll(products);
        }

        sql += " LIMIT 100";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql,
                     ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)) {
            statement.setFetchSize(0);

            int index = 1;
            for (Object param : params) {
                statement.setObject(index++, param);
            }

            try (ResultSet rs = statement.executeQuery()) {
                List<IncomeResponseForDealer> incomes = new ArrayList<>();
                while (rs.next()) {
                    IncomeResponseForDealer income = new IncomeResponseForDealer(
                            rs.getLong("id"),
                            rs.getTimestamp("created_at").toLocalDateTime().toLocalDate(),
                            rs.getBigDecimal("income_amount"),
                            rs.getBigDecimal("percentage_of_profit_for_system"),
                            rs.getBigDecimal("percentage_of_profit_for_dealer"),
                            rs.getBigDecimal("cost"),
                            rs.getBigDecimal("real_cost"),
                            rs.getLong("product_id"),
                            rs.getString("product_name")
                    );
                    incomes.add(income);
                }
                return incomes;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    private Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(dataSourceUrl,dataSourceUsername,dataSourcePassword);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<IncomeResponseForAdmin> getSystemIncomes(LocalDate startDate, LocalDate endDate, List<String> products) {
        log.info("Data fetched using (Jdbc Client) ResultSet");
        String sql = "SELECT i.id, i.created_at, u.email, i.income_amount, " +
                "p.percentage_of_profit_for_system, p.percentage_of_profit_for_dealer, " +
                "pr.cost, pr.real_cost, p.id AS product_id, p.name AS product_name " +
                "FROM users u " +
                "JOIN purchases pr ON u.id = pr.dealer_id " +
                "JOIN products p ON pr.product_id = p.id " +
                "JOIN incomes i ON pr.id = i.purchase_id " +
                "WHERE i.owner_type = 'SYSTEM' ";

        List<Object> params = new ArrayList<>();

        if (startDate != null && endDate != null) {
            sql += "AND i.created_at >= ? AND i.created_at <= ? ";
            params.add(Date.valueOf(startDate));
            params.add(Date.valueOf(endDate));
        } else if (startDate != null) {
            sql += "AND i.created_at >= ? ";
            params.add(Date.valueOf(startDate));
        } else if (endDate != null) {
            sql += "AND i.created_at <= ? ";
            params.add(Date.valueOf(endDate));
        }

        if (products != null && !products.isEmpty()) {
            sql += "AND p.name IN (" + String.join(",", Collections.nCopies(products.size(), "?")) + ")";
            params.addAll(products);
        }

        sql += " LIMIT 1000000";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql,
                     ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)) {
            statement.setFetchSize(0);

            int index = 1;
            for (Object param : params) {
                statement.setObject(index++, param);
            }

            try (ResultSet rs = statement.executeQuery()) {
                List<IncomeResponseForAdmin> incomes = new ArrayList<>();
                while (rs.next()) {
                    IncomeResponseForAdmin income = new IncomeResponseForAdmin(
                            rs.getLong("id"),
                            rs.getTimestamp("created_at").toLocalDateTime().toLocalDate(),
                            rs.getString("email"),
                            rs.getBigDecimal("income_amount"),
                            rs.getBigDecimal("percentage_of_profit_for_system"),
                            rs.getBigDecimal("percentage_of_profit_for_dealer"),
                            rs.getBigDecimal("cost"),
                            rs.getBigDecimal("real_cost"),
                            rs.getLong("product_id"),
                            rs.getString("product_name")
                    );
                    incomes.add(income);
                }
                return incomes;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }



}
