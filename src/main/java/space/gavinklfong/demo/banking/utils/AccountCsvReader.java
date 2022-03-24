package space.gavinklfong.demo.banking.utils;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.experimental.UtilityClass;
import space.gavinklfong.demo.banking.models.Account;
import space.gavinklfong.demo.banking.models.Transaction;
import space.gavinklfong.demo.banking.models.TransactionType;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@UtilityClass
public class AccountCsvReader {

    public Map<String, Account> readAccopuntsFromCSV(String filename) throws IOException, CsvException {
        Map<String, Account> accounts = null;
        try (CSVReader reader = new CSVReader(new FileReader(filename))) {
            reader.skip(1);
            accounts = reader.readAll()
                    .stream()
                    .map(AccountCsvReader::parseFromCsvEntry)
                    .collect(Collectors.toMap(Account::getAccountNumber, Function.identity()));
        }
        return accounts;
    }

    private Account parseFromCsvEntry(String[] fields) {
        if (fields.length < 2) throw new IllegalArgumentException("CSV entry has lesser than 2 fields");

        if (isNull(fields[0]) || fields[0].trim().length() == 0)
            throw new IllegalArgumentException("Null/Empty account number is not allowed");

        return Account.builder()
                .accountNumber(fields[0])
                .balance(Double.parseDouble(fields[1]))
                .build();
    }
}
