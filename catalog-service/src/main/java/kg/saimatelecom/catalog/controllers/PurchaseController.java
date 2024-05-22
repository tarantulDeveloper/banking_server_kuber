package kg.saimatelecom.catalog.controllers;

import jakarta.validation.Valid;
import kg.saimatelecom.catalog.dto.request.PurchaseRequest;
import kg.saimatelecom.catalog.dto.response.MessageResponse;
import kg.saimatelecom.catalog.dto.response.MonthlyPurchaseResponseForManager;
import kg.saimatelecom.catalog.endpoint.PurchaseEndpoint;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/purchases")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PurchaseController {

    PurchaseEndpoint purchaseEndpoint;


    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public MessageResponse purchase(@Valid @RequestBody PurchaseRequest request,
                                    @AuthenticationPrincipal UserDetails userDetails) {
        return purchaseEndpoint.buyProduct(request,userDetails.getUsername());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/system/total")
    public Integer getTotalPurchases() {
        return purchaseEndpoint.getTotalPurchases();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/system/monthly")
    public List<MonthlyPurchaseResponseForManager> getSystemMonthlyIncome(
            @RequestParam(name = "year",required = false) Optional<Integer> year) {
        return purchaseEndpoint.getSystemMonthlyPurchase(year);
    }

}