package space.gavinklfong.demo.banking.utils;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.experimental.UtilityClass;
import space.gavinklfong.demo.banking.models.Transaction;
import space.gavinklfong.demo.banking.models.TransactionType;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@UtilityClass
public class TransactionCsvReader {

    public Map<String, List<Transaction>> readTransactionsFromCSV(String filename) throws IOException, CsvException {
        Map<String, List<Transaction>> transactions = null;
        try (CSVReader reader = new CSVReader(new FileReader(filename))) {
            reader.skip(1);
            transactions = reader.readAll()
                    .stream()
                    .map(TransactionCsvReader::parseFromCsvEntry)
                    .collect(Collectors.groupingBy(Transaction::getAccountNumber));
        }
        return transactions;
    }

    private Transaction parseFromCsvEntry(String[] fields) {
        if (fields.length < 3) throw new IllegalArgumentException("CSV entry has lesser than 3 fields");
        return Transaction.builder()
                .accountNumber(fields[0].trim())
                .transactionType(TransactionType.valueOf(fields[1].trim()))
                .amount(Double.parseDouble(fields[2].trim()))
                .build();
    }
}
