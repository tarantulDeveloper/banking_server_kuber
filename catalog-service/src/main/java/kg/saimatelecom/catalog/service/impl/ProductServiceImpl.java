package kg.saimatelecom.catalog.service.impl;

import kg.saimatelecom.catalog.dto.response.ProductFullInfoResponse;
import kg.saimatelecom.catalog.exceptions.ResourceNotFoundException;
import kg.saimatelecom.catalog.model.Product;
import kg.saimatelecom.catalog.repository.ProductRepository;
import kg.saimatelecom.catalog.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService {

    ProductRepository productRepository;

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAllExisting();
    }

    @Override
    public void delete(Long productId) {
        productRepository.softDelete(productId);
    }

    @Override
    public boolean existsById(Long productId) {
        return productRepository.existsById(productId);
    }

    @Override
    public List<Product> findAllByOwnerEmail(String email) {
        return productRepository.findAllExistingByOwnerEmail(email);
    }

    @Override
    public ProductFullInfoResponse findFullInfoById(Long productId) {
        return productRepository.findFullInfoById(productId);
    }

    @Override
    public List<String> getAllProductsNames() {
        return productRepository.getAllProductsNames();
    }

    @Override
    public List<String> getAllDealerProductsNames(String email) {
        return productRepository.getAllDealerProductsNames(email);
    }

    @Override
    public List<Product> findByFilters(String name, Integer quantity, String isoCode, String dealer, BigDecimal systemPercentage, BigDecimal dealerPercentage) {
        return productRepository.findProductsByFilters(name, quantity, isoCode, dealer, systemPercentage, dealerPercentage);
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
    }

}
