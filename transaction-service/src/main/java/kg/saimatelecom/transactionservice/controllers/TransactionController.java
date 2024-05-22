package kg.saimatelecom.transactionservice.controllers;

import jakarta.validation.Valid;
import kg.saimatelecom.transactionservice.dto.request.TransactionRequest;
import kg.saimatelecom.transactionservice.dto.response.MessageResponse;
import kg.saimatelecom.transactionservice.dto.response.TransactionResponse;
import kg.saimatelecom.transactionservice.endpoint.TransactionEndpoint;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransactionController {

    TransactionEndpoint transactionEndpoint;


    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/transfer")
    public MessageResponse transferMoney(@Valid @RequestBody TransactionRequest transactionRequest,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        return transactionEndpoint.createTransaction(transactionRequest, userDetails.getUsername());
    }


    @PreAuthorize("hasRole('USER')")
    @GetMapping("/my")
    List<TransactionResponse> getPersonalTransactions(@AuthenticationPrincipal UserDetails userDetails,
                                                      @RequestParam(value = "startDate", required = false)
                                                      @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate startDate,
                                                      @RequestParam(value = "endDate", required = false)
                                                      @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate endDate) {

        return transactionEndpoint.getTransactionsInfoByEmail(
                userDetails.getUsername(),
                startDate != null ? Timestamp.valueOf(startDate.atStartOfDay()) : null,
                endDate != null ? Timestamp.valueOf(endDate.atStartOfDay()) : null);
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportTransactionsToExcel(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(value = "startDate", required = false)
            @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate startDate,
            @RequestParam(value = "endDate", required = false)
            @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate endDate) {

        return transactionEndpoint.exportTransactionsToExcel(
                userDetails.getUsername(),
                startDate != null ? Timestamp.valueOf(startDate.atStartOfDay()) : null,
                endDate != null ? Timestamp.valueOf(endDate.atStartOfDay()) : null);
    }
}

