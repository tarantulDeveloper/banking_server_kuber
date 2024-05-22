package kg.saimatelecom.catalog.controllers;



import jakarta.validation.Valid;
import kg.saimatelecom.catalog.dto.request.ProductCreateRequest;
import kg.saimatelecom.catalog.dto.response.MessageResponse;
import kg.saimatelecom.catalog.dto.response.ProductFullInfoResponse;
import kg.saimatelecom.catalog.dto.response.ProductResponseForClient;
import kg.saimatelecom.catalog.dto.response.ProductResponseForManager;
import kg.saimatelecom.catalog.endpoint.ProductEndpoint;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:8819", "http://194.152.37.7:8819"})
@RequestMapping("/api/products")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductController {

    ProductEndpoint productEndpoint;

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public MessageResponse createProduct(@Valid @ModelAttribute ProductCreateRequest productCreateRequest,
                                         @AuthenticationPrincipal UserDetails userDetails,
                                         @RequestHeader("Authorization") String authorizationHeader,
                                         @RequestParam(value = "replicate",required = false) Boolean isReplicate) {
        return productEndpoint.createProduct(productCreateRequest, userDetails.getUsername(),authorizationHeader,isReplicate);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PutMapping("/{productId}")
    public MessageResponse updateProduct(@PathVariable Long productId,
                                         @AuthenticationPrincipal UserDetails userDetails,
                                         @Valid @ModelAttribute ProductCreateRequest productCreateRequest,
                                         @RequestHeader("Authorization") String authorizationHeader,
                                         @RequestParam(value = "replicate",required = false) Boolean isReplicate) {
        return productEndpoint.updateProductById(productId, productCreateRequest, userDetails.getUsername(),authorizationHeader,isReplicate);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @DeleteMapping("/{productId}")
    public MessageResponse deleteProduct(@PathVariable Long productId,
                                         @RequestHeader("Authorization") String authorizationHeader,
                                         @RequestParam(value = "replicate",required = false) Boolean isReplicate) {
        return productEndpoint.deleteProductById(productId,authorizationHeader,isReplicate);
    }


    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @GetMapping("/{productId}")
    public ProductFullInfoResponse getProductById(@PathVariable("productId") Long productId) {
        return productEndpoint.getProductById(productId);
    }


    @PreAuthorize("hasRole('USER')")
    @GetMapping("/myProducts")
    public List<ProductResponseForClient> fetchDealerProducts(
            @AuthenticationPrincipal UserDetails userDetail) {
        return productEndpoint.getAllByOwnerEmail(userDetail.getUsername());
    }

    @GetMapping()
    public List<ProductResponseForClient> fetchAllProducts() {
        return productEndpoint.getAllProducts();
    }

    @GetMapping("/product-names")
    @PreAuthorize("hasRole('ADMIN')")
    public List<String> fetchAllProductsNames() {
        return productEndpoint.getAllProductsNames();
    }

    @GetMapping("/dealer-product-names")
    @PreAuthorize("hasRole('USER')")
    public List<String> fetchAllDealerProductsNames(@AuthenticationPrincipal UserDetails userDetail) {
        return productEndpoint.getAllDealerProductsNames(userDetail.getUsername());
    }

    @GetMapping("/manager")
    public List<ProductResponseForManager> fetchAllProductsForManager(@RequestParam(required = false) String name,
                                                                      @RequestParam(required = false) Integer quantity,
                                                                      @RequestParam(required = false) String isoCode,
                                                                      @RequestParam(required = false) String dealer,
                                                                      @RequestParam(required = false) BigDecimal systemPercentage,
                                                                      @RequestParam(required = false) BigDecimal dealerPercentage) {
        return productEndpoint.getAllProductsForManager(name, quantity, isoCode, dealer, systemPercentage, dealerPercentage);
    }
}

