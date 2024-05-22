package kg.saimatelecom.transactionservice.service.impl;

import kg.saimatelecom.transactionservice.dto.response.TransactionResponse;
import kg.saimatelecom.transactionservice.model.Transaction;
import kg.saimatelecom.transactionservice.repository.TransactionRepository;
import kg.saimatelecom.transactionservice.service.TransactionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransactionServiceImpl implements TransactionService {

    TransactionRepository transactionRepository;

    @Override
    public void save(Transaction transaction) {
        transactionRepository.saveTransaction(transaction);
    }


    @Override
    public List<TransactionResponse> getTransactionInfoByEmail(String email, Timestamp startDate, Timestamp endDate) {
        return transactionRepository.findTransactionsByEmailAndDateRange(email,startDate,endDate);
    }

    public ResponseEntity<byte[]> exportTransactionsToExcel(String email, Timestamp startDate, Timestamp endDate) {
        List<TransactionResponse> transactions = transactionRepository.findTransactionsByEmailAndDateRange(email, startDate, endDate);

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Transactions");
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Sender Email");
            headerRow.createCell(1).setCellValue("Receiver Email");
            headerRow.createCell(2).setCellValue("Amount");
            headerRow.createCell(3).setCellValue("Created At");
            int rowNum = 1;
            for (TransactionResponse transaction : transactions) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(transaction.sender());
                row.createCell(1).setCellValue(transaction.receiver());
                row.createCell(2).setCellValue(transaction.amount().toString());
                row.createCell(3).setCellValue(transaction.createdAt().toString());
            }
            workbook.write(outputStream);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "transactions.xlsx");
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(outputStream.toByteArray());
        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }



}

