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
import java.util.stream.Collectors;

@UtilityClass
public class TransactionCsvReader {

    public List<Transaction> readTransactionsFromCSV(String filename) throws IOException, CsvException {
        List<Transaction> transactions = null;
        try (CSVReader reader = new CSVReader(new FileReader(filename))) {
            reader.skip(1);
            transactions = reader.readAll()
                    .stream()
                    .map(TransactionCsvReader::parseFromCsvEntry)
                    .collect(Collectors.toList());
        }
        return transactions;
    }

    private Transaction parseFromCsvEntry(String[] fields) {
        if (fields.length < 2) throw new IllegalArgumentException("CSV entry has lesser than 2 fields");
        return Transaction.builder()
                .transactionType(TransactionType.valueOf(fields[0]))
                .amount(Double.parseDouble(fields[1]))
                .build();
    }
}
