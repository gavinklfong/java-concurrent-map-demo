const faker = require('@faker-js/faker');
const fs = require('fs');

class Transaction {
    constructor(type, amount) {
        this.type = type;
        this.amount = amount;
    }

    toCSV() {
        return `${this.type}, ${this.amount}`;
    }
}

const generateRandom = (lower, upper) => {
    return Math.floor(lower + (upper - lower) * Math.random());
}

const TXN_TYPES = ["DEPOSIT", "WITHDRAW"];

const OUTPUT_FILE = './data/transactions.csv';
const TOTAL_COUNT = 300;
const AMT_LOWER_BOUND = 10;
const AMT_UPPER_BOUND = 1000;

let txns = [];

for (let i = 0; i < TOTAL_COUNT; i++) {
    let randomIndex = Math.floor(Math.random() * 10) % 2;
    let txn = new Transaction(
        TXN_TYPES[randomIndex],
        generateRandom(AMT_LOWER_BOUND, AMT_UPPER_BOUND));
    
    txns.push(txn);
}

try { 
    fs.unlinkSync(OUTPUT_FILE); 
} catch (err) {}

fs.appendFileSync(OUTPUT_FILE, "TransactionType, Amount" + "\n");

txns.forEach(txn => {
    fs.appendFileSync(OUTPUT_FILE, txn.toCSV() + "\n");
});