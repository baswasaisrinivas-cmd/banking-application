package com.banking.banking_application.util;

import java.util.Random;

public class AccountNumberGenerator {

    private static final Random RANDOM = new Random();

    public static String generateAccountNumber() {
        long ts = System.currentTimeMillis();
        int r = RANDOM.nextInt(9000) + 1000;
        return "ACC" + ts + r;
    }

    public static String generateTransactionId() {
        long ts = System.currentTimeMillis();
        int r = RANDOM.nextInt(900) + 100;
        return "TXN" + ts + r;
    }
}
