package org.skarb.logpile.vertx.field;


import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * User: skarb
 * Date: 15/01/13
 */
public class FileSizeUtilsTest {

    @Test(expected = RuntimeException.class)
    public void testcalculate() {
        assertEquals(2296L, FileSizeUtils.calculate(null, 2296L));
        assertEquals(2 * 1024L, FileSizeUtils.calculate("2k", 2296L));
        assertEquals(3 * 1024L*1024L, FileSizeUtils.calculate("3m", 2296L));
        assertEquals(4 * 1024L*1024L*1024L, FileSizeUtils.calculate("4 g", 2296L));
        assertEquals(1000L,  FileSizeUtils.calculate("1000", 2296L));
        FileSizeUtils.calculate("sss", 2296L);
    }
}
