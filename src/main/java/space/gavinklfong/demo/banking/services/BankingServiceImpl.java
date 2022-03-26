package space.gavinklfong.demo.banking.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import space.gavinklfong.demo.banking.exceptions.RecordNotFoundException;
import space.gavinklfong.demo.banking.exceptions.UnknownTransactionTypeException;
import space.gavinklfong.demo.banking.models.Account;
import space.gavinklfong.demo.banking.models.Transaction;
import space.gavinklfong.demo.banking.models.TransactionType;

import java.util.Map;

import static java.util.Objects.isNull;

@RequiredArgsConstructor
@FieldDefaults(makeFinal=true, level= AccessLevel.PRIVATE)
public class BankingServiceImpl implements BankingService {

    Map<String, Account> accounts;

    @Override
    public Double doTransaction(Transaction transaction) {
        Account account = accounts.get(transaction.getAccountNumber());
        if (isNull(account)) throw new RecordNotFoundException();

        accounts.computeIfPresent(transaction.getAccountNumber(), (key, value) ->
                value.toBuilder()
                        .balance(
                                calculateBalance(transaction.getTransactionType(),
                                                value.getBalance(),
                                                transaction.getAmount())
                        )
                        .build()
        );

        return accounts.get(transaction.getAccountNumber()).getBalance();
    }

    @Override
    public Double getBalance(String accountNumber) {
        Account account = accounts.get(accountNumber);
        if (isNull(account)) throw new RecordNotFoundException();
        return account.getBalance();
    }

    private Double calculateBalance(TransactionType transactionType, Double balance, Double amount) {
        double updatedBalance;
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
