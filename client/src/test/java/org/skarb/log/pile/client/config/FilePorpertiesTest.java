package org.skarb.log.pile.client.config;

import org.junit.Assert;
import org.junit.Test;

/**
 * User: skarb
 * Date: 17/01/13
 */
public class FilePorpertiesTest {

    @Test
    public void testConfigured(){
        PropertiesFile propertiesFile = new PropertiesFile("/test.properties");
        Assert.assertTrue(propertiesFile.configured());
        Assert.assertEquals("application.name",propertiesFile.getApplication());
        Assert.assertEquals("myengine",propertiesFile.getEngineClass());
        Assert.assertEquals("https://truc.fr/event",propertiesFile.getUrl());
    }

}
