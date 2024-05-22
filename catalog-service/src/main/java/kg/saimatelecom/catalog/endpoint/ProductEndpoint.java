package kg.saimatelecom.catalog.endpoint;

import kg.saimatelecom.catalog.dto.request.ProductCreateRequest;
import kg.saimatelecom.catalog.dto.response.*;
import kg.saimatelecom.catalog.exceptions.ResourceNotFoundException;
import kg.saimatelecom.catalog.feign.CurrencyServiceFeignClient;
import kg.saimatelecom.catalog.feign.ImageServiceFeignClient;
import kg.saimatelecom.catalog.feign.UserServiceFeignClient;
import kg.saimatelecom.catalog.mapper.ProductMapper;
import kg.saimatelecom.catalog.model.Catalog;
import kg.saimatelecom.catalog.model.Product;
import kg.saimatelecom.catalog.service.CatalogService;
import kg.saimatelecom.catalog.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductEndpoint {

    ProductService productService;
    ImageServiceFeignClient imageServiceFeignClient;
    ProductMapper productMapper;
    CurrencyServiceFeignClient currencyServiceFeignClient;
    UserServiceFeignClient userServiceFeignClient;
    CatalogService catalogService;


    public List<ProductResponseForClient> getAllProducts() {
        List<Product> products = productService.findAll();
        return getProductResponseForClients(products);
    }

    private List<ProductResponseForClient> getProductResponseForClients(List<Product> products) {
        return products.stream().map(product -> {
            CurrencyResponse currencyResponse = currencyServiceFeignClient.getCurrencyByCurrencyId(
                    product.getCurrencyId()
            );
            BigDecimal currencyValue = currencyResponse.value();
            BigDecimal priceInSom = product.getPrice().add(product.getCommission()).multiply(currencyValue);
            priceInSom = priceInSom.setScale(0, RoundingMode.HALF_UP);
            return new ProductResponseForClient(product.getId(), product.getName(),
                    product.getImageUrl(), product.getQuantity(), priceInSom);
        }).collect(Collectors.toList());
    }

    public List<ProductResponseForClient> getAllByOwnerEmail(String dealerEmail) {
        List<Product> products = productService.findAllByOwnerEmail(dealerEmail);
        return getProductResponseForClients(products);
    }


    public List<ProductResponseForManager> getAllProductsForManager(String name, Integer quantity, String isoCode, String dealer, BigDecimal systemPercentage, BigDecimal dealerPercentage) {
        List<Product> products = productService.findByFilters(name, quantity, isoCode, dealer, systemPercentage, dealerPercentage);
        CurrencyResponse sfc = currencyServiceFeignClient.getCurrencyByIsoCode("SFC");

        return products.stream().map(product -> {
            BigDecimal productCurrencyValue =
                    currencyServiceFeignClient.getCurrencyByCurrencyId(product.getCurrencyId()).value();
            BigDecimal priceInSoftCoin = product.getPrice().add(product.getCommission()).multiply(productCurrencyValue).multiply(sfc.value());
            priceInSoftCoin = priceInSoftCoin.setScale(0, RoundingMode.HALF_UP);
            BigDecimal commissionInSoftCoin = product.getCommission().multiply(productCurrencyValue).multiply(sfc.value());
            commissionInSoftCoin = commissionInSoftCoin.setScale(0, RoundingMode.HALF_UP);
            return new ProductResponseForManager(product.getId(), product.getName(), product.getImageUrl(), priceInSoftCoin, product.getPercentageOfProfitForDealer(), product.getPercentageOfProfitForSystem(), commissionInSoftCoin);
        }).collect(Collectors.toList());
    }

    public MessageResponse deleteProductById(Long productId, String authorizationHeader, Boolean isReplicate) {
        productService.delete(productId);
        return new MessageResponse("Product has been deleted");
    }

    @Transactional
    public MessageResponse updateProductById(Long productId, ProductCreateRequest productCreateRequest,
                                             String managerEmail, String authorizationHeader, Boolean isReplicate) {
        if (productService.existsById(productId)) {
            updateOrCreateProduct(productCreateRequest, managerEmail, productId);
            return new MessageResponse("Product has been updated");
        } else {
            throw new ResourceNotFoundException("Product not found!");
        }
    }


    @Transactional
    public MessageResponse createProduct(ProductCreateRequest productCreateRequest, String managerEmail, String authorizationHeader, Boolean isReplicate) {
        updateOrCreateProduct(productCreateRequest, managerEmail, null);
        return new MessageResponse("Product has been created");
    }

    public MessageResponse updateOrCreateProduct(ProductCreateRequest productCreateRequest,
                                                 String managerEmail,
                                                 Long productId) {
        String createdUrl = imageServiceFeignClient.uploadImage(productCreateRequest.file());
        log.info("Image has been saved to url " + createdUrl);
        Product product = productMapper.toEntity(productCreateRequest);
        CurrencyResponse currency = currencyServiceFeignClient.getCurrencyByIsoCode(productCreateRequest.isoCode());
        product.setImageUrl(createdUrl);
        product.setCurrencyId(currency.id());
        product.setPastRate(currency.value());
        product.setUsername(userServiceFeignClient.getClientFullInfoByEmail(managerEmail).email());
        if (productId != null) {
            product.setId(productId);
        }
        Product savedProduct = productService.saveProduct(product);
        Catalog catalog = catalogService.getCatalogByOwnerEmail(productCreateRequest.dealerEmail()).orElse(
                Catalog.builder()
                        .username(userServiceFeignClient.getClientFullInfoByEmail(managerEmail).email())
                        .products(new ArrayList<>())
                        .build()
        );
        catalog.getProducts().add(savedProduct);
        catalogService.save(catalog);
        return (productId != null) ? new MessageResponse("Product has been updated") :
                new MessageResponse("Product has been created");
    }


    public ProductFullInfoResponse getProductById(Long productId) {
        return productService.findFullInfoById(productId);
    }

    public List<String> getAllProductsNames() {
        return productService.getAllProductsNames();
    }

    public List<String> getAllDealerProductsNames(String email) {
        return productService.getAllDealerProductsNames(email);
    }
}
