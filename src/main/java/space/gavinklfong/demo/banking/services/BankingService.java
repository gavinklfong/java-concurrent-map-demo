package space.gavinklfong.demo.banking.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import space.gavinklfong.demo.banking.exceptions.RecordNotFoundException;
import space.gavinklfong.demo.banking.exceptions.UnknownTransactionTypeException;
import space.gavinklfong.demo.banking.models.Account;
import space.gavinklfong.demo.banking.models.TransactionType;

import java.math.BigDecimal;
import java.util.Map;

import static java.util.Objects.isNull;

@RequiredArgsConstructor
@Service
@FieldDefaults(makeFinal=true, level= AccessLevel.PRIVATE)
public class BankingService {

    Map<String, Account> accounts;

    public Double withdraw(String accountNumber, Double amount) {
        return doTransaction(TransactionType.WITHDRAW, accountNumber, amount);
    }

    public Double deposit(String accountNumber, Double amount) {
        return doTransaction(TransactionType.DEPOSIT, accountNumber, amount);
    }

    public Double getBalance(String accountNumber) {
        Account account = accounts.get(accountNumber);
        if (isNull(account)) throw new RecordNotFoundException();
        return account.getBalance();
    }

    private Double doTransaction(TransactionType transactionType, String accountNumber, Double amount) {
        Account account = accounts.get(accountNumber);
        if (isNull(account)) throw new RecordNotFoundException();

        Double updatedBalance = calculateBalance(transactionType, account.getBalance(), amount);
        account.setBalance(updatedBalance);

        return updatedBalance;
    }

    private Double calculateBalance(TransactionType transactionType, Double balance, Double amount) {
        Double updatedBalance = 0D;
        switch (transactionType) {
            case DEPOSIT:
                updatedBalance = balance + amount;
                break;
            case WITHDRAW:
                updatedBalance = balance - amount;
                break;
            default:
                throw new UnknownTransactionTypeException();
        }
        return updatedBalance;
    }
}
