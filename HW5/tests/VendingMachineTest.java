import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class VendingMachineTest {
    VendingMachine machine;
    static Random rnd = new Random();

    static final long wrongCode = 123L;
    static final long correctCode = 117345294655382L;

    static final int maxCoins1 = 50;
    static final int maxCoins2 = 50;
    static final int coin1value = 1;
    static final int coin2value = 2;

    static final int maxProduct1 = 30;
    static final int maxProduct2 = 40;

    @BeforeEach
    void setUp() {
        machine = new VendingMachine();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getCurrentSum() {
        int coins1 = rnd.ints(0, maxCoins1).findFirst().getAsInt();
        int coins2 = rnd.ints(0, maxCoins2).findFirst().getAsInt();

        machine.enterAdminMode(correctCode);
        machine.fillCoins1(coins1);
        machine.fillCoins2(coins2);

        assertEquals(machine.getCurrentSum(), coins1 * coin1value + coins2 * coin2value);

        machine.exitAdminMode();
        assertEquals(machine.getCurrentSum(), 0);
    }

    @Test
    void getCoins1() {
        int coins1 = rnd.ints(0, maxCoins1).findFirst().getAsInt();
        int coins2 = rnd.ints(0, maxCoins2).findFirst().getAsInt();
        // добавляпм и монеты второго вида, чтобы точно знать, что друг на друга они не влияют
        machine.enterAdminMode(correctCode);
        machine.fillCoins1(coins1);
        machine.fillCoins2(coins2);

        assertEquals(machine.getCoins1(), coins1);

        machine.exitAdminMode();
        assertEquals(machine.getCoins1(), 0);
    }

    @Test
    void getCoins2() {
        int coins1 = rnd.ints(0, maxCoins1).findFirst().getAsInt();
        int coins2 = rnd.ints(0, maxCoins2).findFirst().getAsInt();
        // добавляпм и монеты второго вида, чтобы точно знать, что друг на друга они не влияют
        machine.enterAdminMode(correctCode);
        machine.fillCoins1(coins1);
        machine.fillCoins2(coins2);

        assertEquals(machine.getCoins2(), coins2);

        machine.exitAdminMode();
        assertEquals(machine.getCoins2(), 0);
    }

    @Test
    void getPrice1() {
        int price1 = rnd.ints(0, 999).findFirst().getAsInt();
        int price2 = rnd.ints(0, 999).findFirst().getAsInt();

        assertEquals(machine.getPrice1(), 8);

        machine.enterAdminMode(correctCode);
        assertEquals(machine.getPrice1(), 8);

        machine.setPrice1(price1);
        assertEquals(machine.getPrice1(), price1);
        machine.setPrice2(price2);
        assertEquals(machine.getPrice1(), price1);

        machine.exitAdminMode();
        assertEquals(machine.getPrice1(), price1);
    }

    @Test
    void getPrice2() {
        int price1 = rnd.ints(0, 999).findFirst().getAsInt();
        int price2 = rnd.ints(0, 999).findFirst().getAsInt();

        assertEquals(machine.getPrice2(), 5);

        machine.enterAdminMode(correctCode);
        assertEquals(machine.getPrice2(), 5);

        machine.setPrice2(price2);
        assertEquals(machine.getPrice2(), price2);
        machine.setPrice1(price1);
        assertEquals(machine.getPrice2(), price2);

        machine.exitAdminMode();
        assertEquals(machine.getPrice2(), price2);
    }

    @Test
    void fillProduct1() {
        assertEquals(machine.giveProduct1(1), VendingMachine.Response.INSUFFICIENT_PRODUCT);
        assertEquals(machine.fillProduct1(), VendingMachine.Response.ILLEGAL_OPERATION);
        assertEquals(machine.giveProduct1(1), VendingMachine.Response.INSUFFICIENT_PRODUCT);

        machine.enterAdminMode(correctCode);
        assertEquals(machine.fillProduct1(), VendingMachine.Response.OK);
        assertEquals(machine.getNumberOfProduct1(), maxProduct1);
        machine.exitAdminMode();
        assertEquals(machine.getNumberOfProduct1(), maxProduct1);

        assertNotEquals(machine.giveProduct1(30), VendingMachine.Response.INSUFFICIENT_PRODUCT);
    }

    @Test
    void fillProduct2() {
        assertEquals(machine.giveProduct2(1), VendingMachine.Response.INSUFFICIENT_PRODUCT);
        assertEquals(machine.fillProduct2(), VendingMachine.Response.ILLEGAL_OPERATION);
        assertEquals(machine.giveProduct1(1), VendingMachine.Response.INSUFFICIENT_PRODUCT);

        machine.enterAdminMode(correctCode);
        assertEquals(machine.fillProduct2(), VendingMachine.Response.OK);
        machine.exitAdminMode();

        assertNotEquals(machine.giveProduct2(40), VendingMachine.Response.INSUFFICIENT_PRODUCT);
    }

    @Test
    void fillCoins1() {
        int coins1good = rnd.ints(11, maxCoins1).findFirst().getAsInt();
        int coins1bad_f = maxCoins1 + 10;
        int coins1bad_s = -1;

        assertEquals(machine.fillCoins1(coins1good), VendingMachine.Response.ILLEGAL_OPERATION);

        machine.enterAdminMode(correctCode);
        assertEquals(machine.getCoins1(), 0);
        assertEquals(machine.getCoins2(), 0);
        assertEquals(machine.fillCoins1(coins1bad_f), VendingMachine.Response.INVALID_PARAM);
        assertEquals(machine.fillCoins1(coins1bad_s), VendingMachine.Response.INVALID_PARAM);

        assertEquals(machine.getCoins1(), 0);
        assertEquals(machine.getCoins2(), 0);
        assertEquals(machine.fillCoins1(coins1good), VendingMachine.Response.OK);
        assertEquals(machine.getCoins1(), coins1good);
        assertEquals(machine.getCoins2(), 0);
        // проверим, что значение уменьшается корректно
        assertEquals(machine.fillCoins1(5), VendingMachine.Response.OK);
        assertEquals(machine.getCoins1(), 5);
        assertEquals(machine.getCoins2(), 0);
        // проверим, что значение нельзя опустить до нуля
        assertEquals(machine.fillCoins1(0), VendingMachine.Response.INVALID_PARAM);
        assertEquals(machine.getCoins1(), 5);
        assertEquals(machine.getCoins2(), 0);
    }

    @Test
    void fillCoins2() {
        int coins2good = rnd.ints(11, maxCoins2).findFirst().getAsInt();
        int coins2bad_f = maxCoins2 + 10;
        int coins2bad_s = -1;

        assertEquals(machine.fillCoins2(coins2good), VendingMachine.Response.ILLEGAL_OPERATION);

        machine.enterAdminMode(correctCode);
        assertEquals(machine.getCoins1(), 0);
        assertEquals(machine.getCoins2(), 0);
        assertEquals(machine.fillCoins2(coins2bad_f), VendingMachine.Response.INVALID_PARAM);
        assertEquals(machine.fillCoins2(coins2bad_s), VendingMachine.Response.INVALID_PARAM);

        assertEquals(machine.getCoins1(), 0);
        assertEquals(machine.getCoins2(), 0);
        assertEquals(machine.fillCoins2(coins2good), VendingMachine.Response.OK);
        assertEquals(machine.getCoins2(), coins2good);
        assertEquals(machine.getCoins1(), 0);
        // проверим, что значение уменьшается корректно
        assertEquals(machine.fillCoins2(5), VendingMachine.Response.OK);
        assertEquals(machine.getCoins2(), 5);
        assertEquals(machine.getCoins1(), 0);
        // проверим, что значение нельзя опустить до нуля
        assertEquals(machine.fillCoins2(0), VendingMachine.Response.INVALID_PARAM);
        assertEquals(machine.getCoins2(), 5);
        assertEquals(machine.getCoins1(), 0);
    }

    @Test
    void enterAdminModeBad() {
        assertEquals(machine.getCurrentMode(), VendingMachine.Mode.OPERATION);
        assertEquals(machine.enterAdminMode(wrongCode), VendingMachine.Response.INVALID_PARAM);
        assertEquals(machine.getCurrentMode(), VendingMachine.Mode.OPERATION);

        machine.putCoin1(); // кладем монету, чтобы проверить ошибку входа в отладку при ненулевом балансе
        assertEquals(machine.enterAdminMode(correctCode), VendingMachine.Response.CANNOT_PERFORM);
        assertEquals(machine.getCurrentMode(), VendingMachine.Mode.OPERATION);
    }

    @Test
    void enterAdminModeGood() {
        assertEquals(machine.getCurrentMode(), VendingMachine.Mode.OPERATION);
        assertEquals(machine.enterAdminMode(correctCode), VendingMachine.Response.OK);
        assertEquals(machine.getCurrentMode(), VendingMachine.Mode.ADMINISTERING);
        // проверим, что мы и правда в режиме отладки, вызвав команды администратора
        assertEquals(machine.fillProduct1(), VendingMachine.Response.OK);
        assertEquals(machine.fillProduct2(), VendingMachine.Response.OK);
        assertEquals(machine.fillCoins1(10), VendingMachine.Response.OK);
        assertEquals(machine.fillCoins2(10), VendingMachine.Response.OK);

        assertEquals(machine.getCoins1(), 10);
        assertEquals(machine.getCoins2(), 10);
        assertEquals(machine.getCurrentSum(), 10 * coin1value + 10 * coin2value);
    }

    @Test
    void enterOperationModeFromOperation() {
        assertEquals(machine.getCurrentMode(), VendingMachine.Mode.OPERATION);
        machine.exitAdminMode();
        assertEquals(machine.getCurrentMode(), VendingMachine.Mode.OPERATION);
        // проверим, что мы и правда в режиме отладки, вызвав команды администратора
        assertEquals(machine.fillProduct1(), VendingMachine.Response.ILLEGAL_OPERATION);
        assertEquals(machine.fillProduct2(), VendingMachine.Response.ILLEGAL_OPERATION);
        assertEquals(machine.fillCoins1(10), VendingMachine.Response.ILLEGAL_OPERATION);
        assertEquals(machine.fillCoins2(10), VendingMachine.Response.ILLEGAL_OPERATION);
    }

    @Test
    void enterOperationModeFromAdmin() {
        assertEquals(machine.enterAdminMode(correctCode), VendingMachine.Response.OK);
        assertEquals(machine.getCurrentMode(), VendingMachine.Mode.ADMINISTERING);
        machine.exitAdminMode();
        assertEquals(machine.getCurrentMode(), VendingMachine.Mode.OPERATION);
        // проверим, что мы и правда в режиме отладки, вызвав команды администратора
        assertEquals(machine.fillProduct1(), VendingMachine.Response.ILLEGAL_OPERATION);
        assertEquals(machine.fillProduct2(), VendingMachine.Response.ILLEGAL_OPERATION);
        assertEquals(machine.fillCoins1(10), VendingMachine.Response.ILLEGAL_OPERATION);
        assertEquals(machine.fillCoins2(10), VendingMachine.Response.ILLEGAL_OPERATION);
    }

    @Test
    void setPrice1() {
        int price1 = rnd.ints(9, Integer.MAX_VALUE).findFirst().getAsInt();

        assertEquals(machine.setPrice1(price1), VendingMachine.Response.ILLEGAL_OPERATION);

        machine.enterAdminMode(correctCode);
        assertEquals(machine.getPrice1(), 8);
        assertEquals(machine.getPrice2(), 5);

        assertEquals(machine.setPrice1(0), VendingMachine.Response.INVALID_PARAM);
        assertEquals(machine.setPrice1(-5), VendingMachine.Response.INVALID_PARAM);
        assertEquals(machine.getPrice1(), 8);
        assertEquals(machine.getPrice2(), 5);

        assertEquals(machine.setPrice1(price1), VendingMachine.Response.OK);
        assertEquals(machine.getPrice1(), price1);
        assertEquals(machine.getPrice2(), 5);

        assertEquals(machine.setPrice1(8), VendingMachine.Response.OK);
        assertEquals(machine.getPrice1(), 8);
        assertEquals(machine.getPrice2(), 5);
    }

    @Test
    void setPrice2() {
        int price2 = rnd.ints(9, Integer.MAX_VALUE).findFirst().getAsInt();

        assertEquals(machine.setPrice2(price2), VendingMachine.Response.ILLEGAL_OPERATION);

        machine.enterAdminMode(correctCode);
        assertEquals(machine.getPrice1(), 8);
        assertEquals(machine.getPrice2(), 5);

        assertEquals(machine.setPrice2(0), VendingMachine.Response.INVALID_PARAM);
        assertEquals(machine.setPrice2(-5), VendingMachine.Response.INVALID_PARAM);
        assertEquals(machine.getPrice1(), 8);
        assertEquals(machine.getPrice2(), 5);

        assertEquals(machine.setPrice2(price2), VendingMachine.Response.OK);
        assertEquals(machine.getPrice2(), price2);
        assertEquals(machine.getPrice1(), 8);

        assertEquals(machine.setPrice2(5), VendingMachine.Response.OK);
        assertEquals(machine.getPrice1(), 8);
        assertEquals(machine.getPrice2(), 5);
    }

    @Test
    void putCoin1() {
        assertEquals(machine.putCoin1(), VendingMachine.Response.OK);
        assertEquals(machine.getCurrentBalance(), 1 * coin1value + 0 * coin2value);

        assertEquals(machine.putCoin1(), VendingMachine.Response.OK);
        assertEquals(machine.putCoin2(), VendingMachine.Response.OK);
        assertEquals(machine.putCoin1(), VendingMachine.Response.OK);
        assertEquals(machine.getCurrentBalance(), 3 * coin1value + 1 * coin2value);
        machine.returnMoney();

        machine.enterAdminMode(correctCode);
        assertEquals(machine.putCoin1(), VendingMachine.Response.ILLEGAL_OPERATION);
        machine.fillCoins1(maxCoins1);
        machine.exitAdminMode();
        // проверяем добавление монетки в заполненный автомат
        assertEquals(machine.putCoin1(), VendingMachine.Response.CANNOT_PERFORM);
    }

    @Test
    void putCoin2() {
        assertEquals(machine.putCoin2(), VendingMachine.Response.OK);
        assertEquals(machine.getCurrentBalance(), 0 * coin1value + 1 * coin2value);

        assertEquals(machine.putCoin2(), VendingMachine.Response.OK);
        assertEquals(machine.putCoin1(), VendingMachine.Response.OK);
        assertEquals(machine.putCoin2(), VendingMachine.Response.OK);
        assertEquals(machine.getCurrentBalance(), 1 * coin1value + 3 * coin2value);
        machine.returnMoney();

        machine.enterAdminMode(correctCode);
        assertEquals(machine.putCoin2(), VendingMachine.Response.ILLEGAL_OPERATION);
        machine.fillCoins2(maxCoins2);
        machine.exitAdminMode();
        // проверяем добавление монетки в заполненный автомат
        assertEquals(machine.putCoin2(), VendingMachine.Response.CANNOT_PERFORM);
    }

    @Test
    void returnMoneyAccess() {
        assertEquals(machine.returnMoney(), VendingMachine.Response.OK);
        assertEquals(machine.getCurrentBalance(), 0);

        machine.enterAdminMode(correctCode);
        assertEquals(machine.returnMoney(), VendingMachine.Response.ILLEGAL_OPERATION);

        machine.exitAdminMode();
        assertEquals(machine.returnMoney(), VendingMachine.Response.OK);
        assertEquals(machine.getCurrentBalance(), 0);
    }

    @Test
    void returnMoneyWithBothCoinsLackOfCoins2() {
        machine.enterAdminMode(correctCode);
        machine.fillCoins2(2);
        machine.exitAdminMode();

        for (int i = 0; i < 5; ++i) {
            machine.putCoin1();
        }

        assertEquals(machine.returnMoney(), VendingMachine.Response.OK);
        assertEquals(machine.getCurrentBalance(), 0);

        machine.enterAdminMode(correctCode);
        assertEquals(machine.getCoins1(), 4);
        assertEquals(machine.getCoins2(), 0);
        machine.exitAdminMode();
    }

    @Test
    void returnMoneyWithBothCoinsManyCoins2() {
        machine.enterAdminMode(correctCode);
        machine.fillCoins2(3);
        machine.exitAdminMode();

        for (int i = 0; i < 5; ++i) {
            machine.putCoin1();
        }

        assertEquals(machine.returnMoney(), VendingMachine.Response.OK);
        assertEquals(machine.getCurrentBalance(), 0);

        machine.enterAdminMode(correctCode);
        assertEquals(machine.getCoins1(), 4);
        assertEquals(machine.getCoins2(), 1);
        machine.exitAdminMode();
    }

    @Test
    void returnMoneyWithCoins2() {
        machine.enterAdminMode(correctCode);
        machine.fillCoins2(5);
        machine.exitAdminMode();

        for (int i = 0; i < 4; ++i) {
            machine.putCoin1();
        }
        machine.putCoin2();

        assertEquals(machine.returnMoney(), VendingMachine.Response.OK);
        assertEquals(machine.getCurrentBalance(), 0);

        machine.enterAdminMode(correctCode);
        assertEquals(machine.getCoins1(), 4);
        assertEquals(machine.getCoins2(), 3);
        machine.exitAdminMode();
    }

    @Test
    void giveProduct1Access() {
        assertEquals(machine.giveProduct1(1), VendingMachine.Response.INSUFFICIENT_PRODUCT);
        assertEquals(machine.getNumberOfProduct1(), 0);

        machine.enterAdminMode(correctCode);
        machine.fillProduct1();
        assertEquals(machine.giveProduct1(1), VendingMachine.Response.ILLEGAL_OPERATION);
        assertEquals(machine.getNumberOfProduct1(), maxProduct1);
        machine.exitAdminMode();

        assertEquals(machine.giveProduct1(0), VendingMachine.Response.INVALID_PARAM);
        assertEquals(machine.giveProduct1(-5), VendingMachine.Response.INVALID_PARAM);
        assertEquals(machine.giveProduct1(maxProduct1 + 1), VendingMachine.Response.INVALID_PARAM);
        assertEquals(machine.giveProduct1(1), VendingMachine.Response.INSUFFICIENT_MONEY);
        assertEquals(machine.getNumberOfProduct1(), maxProduct1);

        for (int i = 0; i < 4; ++i) {
            machine.putCoin2();
        }
        assertEquals(machine.giveProduct1(1), VendingMachine.Response.OK);
        assertEquals(machine.getNumberOfProduct1(), maxProduct1 - 1);
        assertEquals(machine.getCurrentBalance(), 0);
    }

    @Test
    void giveProduct1UnsuitableChange() {
        machine.enterAdminMode(correctCode);
        machine.setPrice1(7);
        machine.fillProduct1();
        machine.fillCoins2(5);
        machine.exitAdminMode();

        for (int i = 0; i < 4; ++i) {
            machine.putCoin2();
        }

        assertEquals(machine.giveProduct1(1), VendingMachine.Response.UNSUITABLE_CHANGE);
        assertEquals(machine.getNumberOfProduct1(), maxProduct1);
    }

    @Test
    void giveProduct1BothCoinsLackOfType2() {
        machine.enterAdminMode(correctCode);
        machine.fillProduct1();
        machine.fillCoins2(1);
        machine.exitAdminMode();

        for (int i = 0; i < 13; ++i) {
            machine.putCoin1();
        }

        assertEquals(machine.giveProduct1(1), VendingMachine.Response.OK);
        assertEquals(machine.getNumberOfProduct1(), maxProduct1 - 1);
        assertEquals(machine.getCurrentBalance(), 0);

        machine.enterAdminMode(correctCode);
        assertEquals(machine.getCoins1(), 10);
        assertEquals(machine.getCoins2(), 0);
        machine.exitAdminMode();
    }

    @Test
    void giveProduct1BothCoinsManyType2() {
        machine.enterAdminMode(correctCode);
        machine.fillProduct1();
        machine.fillCoins2(4);
        machine.exitAdminMode();

        for (int i = 0; i < 13; ++i) {
            machine.putCoin1();
        }

        assertEquals(machine.giveProduct1(1), VendingMachine.Response.OK);
        assertEquals(machine.getNumberOfProduct1(), maxProduct1 - 1);
        assertEquals(machine.getCurrentBalance(), 0);

        machine.enterAdminMode(correctCode);
        assertEquals(machine.getCoins1(), 12);
        assertEquals(machine.getCoins2(), 2);
        machine.exitAdminMode();
    }

    @Test
    void giveProduct2Access() {
        assertEquals(machine.giveProduct2(1), VendingMachine.Response.INSUFFICIENT_PRODUCT);
        assertEquals(machine.getNumberOfProduct2(), 0);

        machine.enterAdminMode(correctCode);
        machine.fillProduct2();
        assertEquals(machine.giveProduct2(1), VendingMachine.Response.ILLEGAL_OPERATION);
        assertEquals(machine.getNumberOfProduct2(), maxProduct2);
        machine.exitAdminMode();

        assertEquals(machine.giveProduct2(0), VendingMachine.Response.INVALID_PARAM);
        assertEquals(machine.giveProduct2(-5), VendingMachine.Response.INVALID_PARAM);
        assertEquals(machine.giveProduct2(maxProduct2 + 1), VendingMachine.Response.INVALID_PARAM);
        assertEquals(machine.giveProduct2(1), VendingMachine.Response.INSUFFICIENT_MONEY);
        assertEquals(machine.getNumberOfProduct2(), maxProduct2);

        for (int i = 0; i < 5; ++i) {
            machine.putCoin1();
        }
        assertEquals(machine.giveProduct2(1), VendingMachine.Response.OK);
        assertEquals(machine.getNumberOfProduct2(), maxProduct2 - 1);
        assertEquals(machine.getCurrentBalance(), 0);
    }

    @Test
    void giveProduct2UnsuitableChange() {
        machine.enterAdminMode(correctCode);
        machine.setPrice2(7);
        machine.fillProduct2();
        machine.fillCoins2(5);
        machine.exitAdminMode();

        for (int i = 0; i < 4; ++i) {
            machine.putCoin2();
        }

        assertEquals(machine.giveProduct2(1), VendingMachine.Response.UNSUITABLE_CHANGE);
        assertEquals(machine.getNumberOfProduct2(), maxProduct2);
    }

    @Test
    void giveProduct2BothCoinsLackOfType2() {
        machine.enterAdminMode(correctCode);
        machine.setPrice2(8);
        machine.fillProduct2();
        machine.fillCoins2(1);
        machine.exitAdminMode();

        for (int i = 0; i < 13; ++i) {
            machine.putCoin1();
        }

        assertEquals(machine.giveProduct2(1), VendingMachine.Response.OK);
        assertEquals(machine.getNumberOfProduct2(), maxProduct2 - 1);
        assertEquals(machine.getCurrentBalance(), 0);

        machine.enterAdminMode(correctCode);
        assertEquals(machine.getCoins1(), 10);
        assertEquals(machine.getCoins2(), 0);
        machine.exitAdminMode();
    }

    @Test
    void giveProduct2BothCoinsManyType2() {
        machine.enterAdminMode(correctCode);
        machine.setPrice2(8);
        machine.fillProduct2();
        machine.fillCoins2(4);
        machine.exitAdminMode();

        for (int i = 0; i < 13; ++i) {
            machine.putCoin1();
        }

        assertEquals(machine.giveProduct2(1), VendingMachine.Response.OK);
        assertEquals(machine.getNumberOfProduct2(), maxProduct2 - 1);
        assertEquals(machine.getCurrentBalance(), 0);

        machine.enterAdminMode(correctCode);
        assertEquals(machine.getCoins1(), 12);
        assertEquals(machine.getCoins2(), 2);
        machine.exitAdminMode();
    }
}