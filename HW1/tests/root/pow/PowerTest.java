package root.pow;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PowerTest {
    Power p = new Power();

    @Test
    void notZeroPowTest() {
        for (int i = 1; i < 10_000; i++) {
            assertNotEquals(p.pow(i, 1), 0);
        }
    }

    @Test
    void simplePowTest() {
        for (int i = 1; i <= 10_000; i++) {
            assertEquals(p.pow(i, 2), Math.pow(i, 2));
        }
    }

    @Test
    void borderPowTest() {
        assertEquals(p.pow(2, 31), Math.pow(2, 31));
    }

    @Test
    void modulePowTest() {
        for (int i = 1; i <= 10_000; i++) {
            assertEquals(p.pow(i, 3), Math.pow(i, 3) % 4294967296L);
        }
    }

    @Test
    void negativeBPowTest() {
        for (int i = 1; i <= 10_000; i++) {
            assertEquals(p.pow(i, -i), 1);
        }
    }
}