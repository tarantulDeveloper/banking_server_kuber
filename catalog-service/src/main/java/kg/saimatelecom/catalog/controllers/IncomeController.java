package kg.saimatelecom.catalog.controllers;

import kg.saimatelecom.catalog.dto.response.IncomeResponseForAdmin;
import kg.saimatelecom.catalog.dto.response.IncomeResponseForDealer;
import kg.saimatelecom.catalog.dto.response.MonthlyIncomeResponseForManager;
import kg.saimatelecom.catalog.endpoint.IncomeEndpoint;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/incomes")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IncomeController {

    IncomeEndpoint incomeEndpoint;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/system")
    public List<IncomeResponseForAdmin> getSystemIncomes(
            @RequestParam(name = "isoCode", required = false, defaultValue = "SFC") String isoCode,
            @RequestParam(name = "startDate",required = false) LocalDate startDate,
            @RequestParam(name = "endDate",required = false) LocalDate endDate,
            @RequestParam(name = "products",required = false) List<String> products
    ) {
        return incomeEndpoint.getAllSystemIncomes(isoCode,startDate,endDate,products);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/system/total")
    public BigDecimal getSystemIncomesSum(@RequestParam(value = "isoCode", required = false, defaultValue= "SFC") String isoCode) {
        return incomeEndpoint.getAllSystemIncomesSum(isoCode);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/system/monthly")
    public List<MonthlyIncomeResponseForManager> getSystemMonthlyIncome(
            @RequestParam(name = "year",required = false) Optional<Integer> year,
            @RequestParam(name = "isoCode", required = false, defaultValue = "SFC") String isoCode) {
        return incomeEndpoint.getSystemMonthlyIncome(year, isoCode);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/system/export")
    public ResponseEntity<byte[]> exportSystemIncomes(
            @RequestParam(name = "isoCode", required = false, defaultValue = "SFC") String isoCode,
            @RequestParam(name = "startDate",required = false) LocalDate startDate,
            @RequestParam(name = "endDate",required = false) LocalDate endDate,
            @RequestParam(name = "products",required = false) List<String> products) {
        return incomeEndpoint.exportSystemIncomes(isoCode,startDate,endDate,products);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/dealer/export")
    public ResponseEntity<byte[]> exportDealerIncomes(
            @RequestParam(name = "isoCode", required = false, defaultValue = "SFC") String isoCode,
            @RequestParam(name = "startDate",required = false) LocalDate startDate,
            @RequestParam(name = "endDate",required = false) LocalDate endDate,
            @RequestParam(name = "products",required = false) List<String> products,
            @AuthenticationPrincipal UserDetails userDetails) {
        return incomeEndpoint.exportDealerIncomes(isoCode,startDate,endDate,products,userDetails.getUsername());
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/dealer")
    public List<IncomeResponseForDealer> getDealerIncomes(
            @RequestParam(name = "isoCode", required = false, defaultValue = "SFC") String isoCode,
            @RequestParam(name = "startDate",required = false) LocalDate startDate,
            @RequestParam(name = "endDate",required = false) LocalDate endDate,
            @RequestParam(name = "products",required = false) List<String> products,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return incomeEndpoint.getAllDealerIncomes(isoCode,startDate,endDate,products,userDetails.getUsername());
    }
}
