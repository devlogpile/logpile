package org.skarb.log.pile.client.util;

import org.junit.Test;
import org.skarb.log.pile.client.post.engine.Engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class JavaUtilLogDataTest {

    @Test
    public void testGetApplication() {
        System.setProperty(NameOfConfigurationParameters.PROPERTIES_APPLICATION, "");
        JavaUtilLogData instance = JavaUtilLogData.getInstance();
        assertNotNull(instance);
        assertNotNull(instance.getApplication());
        assertNotNull(instance.getEngine());


        Engine mock = mock(Engine.class);
        instance.setApplication("ddd");
        assertEquals("ddd", instance.getApplication()
        );
        instance.setEngine(mock);
        assertEquals(mock, instance.getEngine()
        );

    }


}
