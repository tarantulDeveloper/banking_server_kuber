package kg.saimatelecom.catalog.repository;

import kg.saimatelecom.catalog.dto.response.ProductFullInfoResponse;
import kg.saimatelecom.catalog.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT * FROM products p WHERE p.deleted = false AND p.quantity > 0",nativeQuery = true)
    List<Product> findAllExisting();

    @Modifying
    @Transactional
    @Query(value = "UPDATE products  SET deleted = true WHERE id = ?",nativeQuery = true)
    void softDelete(Long productId);


    @Query(value = "SELECT p FROM Product p " +
            "JOIN Catalog c ON p MEMBER OF c.products " +
            "JOIN c.username u " +
            "WHERE u = :email " +
            "AND p.deleted = false " +
            "AND p.quantity > 0")
    List<Product> findAllExistingByOwnerEmail(@Param("email") String email);



    @Query(value = "SELECT NEW kg.saimatelecom.catalog.dto.response.ProductFullInfoResponse(" +
            "p.id,p.name,p.imageUrl,p.price,p.percentageOfProfitForDealer,p.percentageOfProfitForSystem," +
            "p.commission,p.quantity,p.currencyId,u.email) FROM Product p,Catalog c" +
            " WHERE  p MEMBER OF c.products AND p.id = :productId")
    ProductFullInfoResponse findFullInfoById(@Param("productId")Long productId);

    @Query(value = "SELECT p.name FROM products p",nativeQuery = true)
    List<String> getAllProductsNames();

    @Query(value = "SELECT p.name FROM Product p " +
            "JOIN Catalog c ON p MEMBER OF c.products " +
            "JOIN c.username u " +
            "WHERE u = :email")
    List<String> getAllDealerProductsNames(String email);


    @Query(value = "SELECT p FROM Product p " +
            "JOIN Catalog c ON p MEMBER OF c.products " +
            "JOIN c.username u " +
            "WHERE (LOWER(:name) IS NULL OR p.name LIKE CONCAT(CAST(LOWER(:name) AS string), '%')) " +
            "AND (:quantity IS NULL OR p.quantity >= :quantity) " +
            "AND (:isoCode IS NULL OR p.currencyId = :isoCode) " +
            "AND (:dealerEmail IS NULL OR u = :dealerEmail) "+
            "AND (:systemPercentage IS NULL OR p.percentageOfProfitForSystem >= :systemPercentage) "+
            "AND (:dealerPercentage IS NULL OR p.percentageOfProfitForDealer >= :dealerPercentage) "
    )
    List<Product> findProductsByFilters(@Param("name") String name,
                                        @Param("quantity") Integer quantity,
                                        @Param("isoCode") String isoCode,
                                        @Param("dealerEmail") String dealerEmail,
                                        @Param("systemPercentage") BigDecimal systemPercentage,
                                        @Param("dealerPercentage") BigDecimal dealerPercentage
    );

}
