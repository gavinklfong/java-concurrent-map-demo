const faker = require('@faker-js/faker');
const fs = require('fs');

class Transaction {
    constructor(accountNumber, type, amount) {
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
    }

    toCSV() {
        return `${this.accountNumber}, ${this.type}, ${this.amount}`;
    }
}

const generateRandom = (lower, upper) => {
    return Math.floor(lower + (upper - lower) * Math.random());
}

const generateRandomForArray = (size) => {
    return Math.floor(Math.random() * 10 * size) % size;
}

// const ACCOUNT_NUMBERS = ["001-123456-001", "001-123456-002", "001-123456-003"];
const ACCOUNT_NUMBERS = ["001-123456-001"];
const TXN_TYPES = ["DEPOSIT", "WITHDRAW"];

const OUTPUT_FILE = './data/transactions.csv';
const TOTAL_COUNT = 1000000;
const AMT_LOWER_BOUND = 10;
const AMT_UPPER_BOUND = 1000;

let txns = [];

for (let i = 0; i < TOTAL_COUNT; i++) {
    let txn = new Transaction(
        ACCOUNT_NUMBERS[generateRandomForArray(ACCOUNT_NUMBERS.length)],
        TXN_TYPES[generateRandomForArray(TXN_TYPES.length)],
        generateRandom(AMT_LOWER_BOUND, AMT_UPPER_BOUND)
    );
    
    txns.push(txn);
}

try { 
    fs.unlinkSync(OUTPUT_FILE); 
} catch (err) {}

fs.appendFileSync(OUTPUT_FILE, "AccountNumber, TransactionType, Amount" + "\n");

txns.forEach(txn => {
    fs.appendFileSync(OUTPUT_FILE, txn.toCSV() + "\n");
});