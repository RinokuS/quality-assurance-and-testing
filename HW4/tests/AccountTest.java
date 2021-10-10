import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {
    Account account;

    @BeforeEach
    void set() {
        account = new Account();
    }

    @Test
    void getBalanceTest() {
        int balance = 100;
        account.deposit(balance);
        assertEquals(account.balance, account.getBalance());
    }

    @Test
    void depositTest() {
        int balance = 100;
        boolean answer = account.deposit(balance);
        assertTrue(answer);
        assertEquals(balance, account.getBalance());
    }

    @Test
    void withdrawTest() {
        int balance = 100;
        account.deposit(balance);
        boolean answer = account.withdraw(balance / 2);
        assertTrue(answer);
        assertEquals(balance / 2, account.getBalance());
    }

    @Test
    void getMaxCreditTest() {
        assertEquals(account.maxCredit, -account.getMaxCredit());
    }

    @Test
    void setMaxCredit() {
        int maxCredit = 1000;
        account.block();
        boolean answer = account.setMaxCredit(maxCredit);
        assertTrue(answer);
        assertEquals(maxCredit, account.getMaxCredit());
    }

    @Test
    void isBlockedTest() {
        assertEquals(account.blocked, account.isBlocked());
    }

    @Test
    void block() {
        account.block();
        assertTrue(account.isBlocked());
    }

    @Test
    void unblockUnSuccess() {
        account.block();
        boolean answer = account.unblock();
        assertFalse(answer);
        assertTrue(account.isBlocked());
    }

    @Test
    void unblockSuccess() {
        account.block();
        account.balance = 1000;
        boolean answer = account.unblock();
        assertTrue(answer);
        assertFalse(account.isBlocked());
    }

    @Test
    void bigWithdrawTest() {
        int balance = 100;
        account.deposit(balance);
        boolean answer = account.withdraw(balance + account.getMaxCredit() + 100);
        assertFalse(answer);
        assertEquals(balance, account.getBalance());
    }

    @Test
    void unblockedSetMaxCreditTest() {
        int maxCredit = account.getMaxCredit();
        boolean res = account.setMaxCredit(100);
        assertFalse(res);
        assertEquals(maxCredit, account.getMaxCredit());
    }

    @Test
    void moreBoundSetMaxCreditTest() {
        account.block();
        boolean answer = account.setMaxCredit(1000001);
        assertFalse(answer);
    }

    @Test
    void lessBoundSetMaxCreditTest() {
        account.block();
        boolean answer = account.setMaxCredit(-1000001);
        assertFalse(answer);
    }

    @Test
    void negativeWithdrawTest() {
        int balance = account.balance;
        assertFalse(account.withdraw(-100));
        assertEquals(balance, account.getBalance());
    }

    @Test
    void negativeDepositTest() {
        int balance = account.balance;
        assertFalse(account.deposit(-100));
        assertEquals(balance, account.getBalance());
    }

    @Test
    void boundDepositTest() {
        int balance = account.balance;
        assertFalse(account.deposit(account.bound + 100));
        assertEquals(balance, account.getBalance());
    }

    @Test
    void boundWithdrawTest() {
        int balance = account.balance;
        assertFalse(account.withdraw(account.bound + 100));
        assertEquals(balance, account.getBalance());
    }

    @Test
    void overBoundDepositTest() {
        int balance = account.bound - 50;
        account.balance = account.bound - 50;
        assertFalse(account.deposit(100));
        assertEquals(balance, account.getBalance());
    }

    @Test
    void overBoundWithdrawTest() {
        account.block();
        account.setMaxCredit(account.bound);
        account.unblock();

        int balance = -(account.bound - 50);
        account.balance = -(account.bound - 50);
        assertFalse(account.withdraw(100));
        assertEquals(balance, account.getBalance());
    }

    @Test
    void blockDepositTest() {
        int balance = account.balance;
        account.block();
        assertFalse(account.deposit(100));
        assertEquals(balance, account.getBalance());
    }

    @Test
    void blockWithdrawTest() {
        int balance = account.balance;
        account.block();
        assertFalse(account.withdraw(100));
        assertEquals(balance, account.getBalance());
    }
}