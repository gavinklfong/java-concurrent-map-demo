package space.gavinklfong.demo.banking.services;

import space.gavinklfong.demo.banking.models.Transaction;

public interface BankingService {
    Double doTransaction(Transaction transaction);
    Double getBalance(String accountNumber);
}
