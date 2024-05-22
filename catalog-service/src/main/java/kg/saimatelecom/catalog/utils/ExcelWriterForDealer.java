package kg.saimatelecom.catalog.utils;

import kg.saimatelecom.catalog.dto.response.IncomeResponseForDealer;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ExcelWriterForDealer {

    private static final int ROWS_PER_SHEET = 1000000;

    public ResponseEntity<byte[]> export(List<IncomeResponseForDealer> objects) {
        log.info("Size of data is {}", objects.size());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (Workbook wb = new Workbook(outputStream, "Software Updater", "1.0")) {
            int numSheets = (int) Math.ceil((double) objects.size() / ROWS_PER_SHEET);
            int numThreads = Math.min(Runtime.getRuntime().availableProcessors(), 30);
            ExecutorService executor = Executors.newFixedThreadPool(numThreads);

            for (int sheetIndex = 0; sheetIndex < numSheets; sheetIndex++) {
                int startIndex = sheetIndex * ROWS_PER_SHEET;
                int endIndex = Math.min((sheetIndex + 1) * ROWS_PER_SHEET, objects.size());
                List<IncomeResponseForDealer> subList = objects.subList(startIndex, endIndex);
                int finalSheetIndex = sheetIndex;
                executor.submit(() -> writeToSheet(wb, subList, finalSheetIndex + 1));
            }

            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (IOException | InterruptedException e) {
            log.error("Error exporting data to Excel: {}", e.getMessage());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "incomes.xlsx");
        return ResponseEntity.ok().headers(headers).body(outputStream.toByteArray());
    }

    private void writeToSheet(Workbook wb, List<IncomeResponseForDealer> data, int sheetIndex) {
        Worksheet ws = wb.newWorksheet("Sheet " + sheetIndex);
        createHeaders(ws);

        for (int i = 0; i < data.size(); i++) {
            createDataRow(ws, i + 1, data.get(i));
        }
    }

    private void createHeaders(Worksheet ws) {
        ws.value(0, 0, "Id");
        ws.value(0, 1, "Created_At");
        ws.value(0, 2, "Income Amount");
        ws.value(0, 3, "System Portion");
        ws.value(0, 4, "Dealer Portion");
        ws.value(0, 5, "Cost");
        ws.value(0, 6, "Real Cost");
        ws.value(0, 7, "Product Id");
        ws.value(0, 8, "Product Name");
    }

    private void createDataRow(Worksheet ws, int rowIndex, IncomeResponseForDealer incomeResponseForDealer) {
        ws.value(rowIndex, 0, incomeResponseForDealer.id());
        ws.value(rowIndex, 1, incomeResponseForDealer.createdAt().toString());
        ws.value(rowIndex, 2, incomeResponseForDealer.incomeAmount());
        ws.value(rowIndex, 3, incomeResponseForDealer.systemPortion());
        ws.value(rowIndex, 4, incomeResponseForDealer.dealerPortion());
        ws.value(rowIndex, 5, incomeResponseForDealer.cost());
        ws.value(rowIndex, 6, incomeResponseForDealer.realCost());
        ws.value(rowIndex, 7, incomeResponseForDealer.productId());
        ws.value(rowIndex, 8, incomeResponseForDealer.productName());
    }
}


