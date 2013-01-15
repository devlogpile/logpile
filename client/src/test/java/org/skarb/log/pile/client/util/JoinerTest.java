package org.skarb.log.pile.client.util;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Test of the method Joiner.
 * User: skarb
 * Date: 30/12/12
 */
public class JoinerTest {

    @Test
    public void testonString() throws Exception {
        Joiner on = Joiner.on("|");
        assertNotNull(on);
        String result = on.join(new String[]{"1", "2"});
        assertEquals("1|2", result);

        StringBuilder stringBuilder = on.appendTo(new StringBuilder("TEST:"), new String[]{"1", "2"});
        assertEquals("TEST:1|2", stringBuilder.toString());
    }

    @Test
    public void testappendTo() throws Exception {
        Joiner on = Joiner.on('|');
        StringBuilder stringBuilder = on.appendTo(new StringBuilder("TEST:"), new String[]{"1", "2"});
        assertEquals("TEST:1|2", stringBuilder.toString());
        StringBuilder stringBuilder2 = on.appendTo(new StringBuilder("TEST:"), "1", "2");
        assertEquals("TEST:1|2", stringBuilder2.toString());

        StringBuilder stringBuilder3 = on.appendTo(new StringBuilder("TEST:"), Arrays.asList("1", "2"));
        assertEquals("TEST:1|2", stringBuilder3.toString());

        StringBuilder stringBuilder4 = on.skipNulls().appendTo(new StringBuilder("TEST:"), Arrays.asList("1", null, "2"));
        assertEquals("TEST:1|2", stringBuilder4.toString());
    }

    @Test
    public void testMapJoiner() throws Exception {
        final Joiner.MapJoiner on = Joiner.on('&').withKeyValueSeparator("=");
        final HashMap<String, String> map = new HashMap<String, String>();
        map.put("param1", "val1");
        map.put("param2", "val2");
        String result = on.join(map);
        assertEquals("param1=val1&param2=val2", result);
        assertEquals("TEST:param1=val1&param2=val2", on.appendTo(new StringBuilder("TEST:"), map).toString());
        map.put("param3", null);
        assertEquals("param1=val1&param2=val2&param3=tmp", on.useForNull("tmp").join(map));


    }
}
