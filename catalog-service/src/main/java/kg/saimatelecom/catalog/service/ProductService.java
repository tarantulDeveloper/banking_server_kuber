package kg.saimatelecom.catalog.service;

import kg.saimatelecom.catalog.dto.response.ProductFullInfoResponse;
import kg.saimatelecom.catalog.model.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    Product saveProduct(Product product);

    List<Product> findAll();

    Product getProductById(Long id);

    void delete(Long productId);

    boolean existsById(Long productId);

    List<Product> findAllByOwnerEmail(String email);

    ProductFullInfoResponse findFullInfoById(Long productId);

    List<String> getAllProductsNames();

    List<String> getAllDealerProductsNames(String email);

    List<Product> findByFilters(String name, Integer quantity, String isoCode, String dealer, BigDecimal systemPercentage, BigDecimal dealerPercentage);
}
