package org.skarb.log.pile.client.config;

import org.junit.Assert;
import org.junit.Test;

import java.net.InetAddress;

/**
 * User: skarb
 * Date: 17/01/13
 */
public class FilePorpertiesTest {

    @Test
    public void testgetMandatoryServerId(){
        final PropertiesFile propertiesFile = new PropertiesFile("/test");
        Assert.assertEquals("test", propertiesFile.getMandatoryServerId("test"));
        String serverId = "";
        try{
        serverId = InetAddress.getLocalHost().getHostName();
        }catch(Exception ex){
              //NOP
        }
        Assert.assertEquals(serverId,propertiesFile.getMandatoryServerId(null));
    }

    @Test
    public void testConfigured(){
        PropertiesFile propertiesFile = new PropertiesFile("/test.properties");
        Assert.assertTrue(propertiesFile.configured());
        Assert.assertEquals("application.name",propertiesFile.getApplication());
        Assert.assertEquals("myengine",propertiesFile.getEngineClass());
        Assert.assertEquals("https://truc.fr/event",propertiesFile.getUrl());
        Assert.assertEquals("my server name",propertiesFile.getServerId());
    }

}
