package com.example.stickhero;

import org.junit.Test;
import static org.junit.Assert.fail;

public class JUnitTest {
    @Test
    public void testCherries() {
        if (StickHeroApplication.getCherry() < 0) {
            fail();
        }
    }

    @Test
    public void testHighScore() {
        if (StickHeroApplication.getHighScore() < 0) {
            fail();
        }
    }
}
