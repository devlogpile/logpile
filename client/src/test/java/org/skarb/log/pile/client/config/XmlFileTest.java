package org.skarb.log.pile.client.config;


import org.junit.Assert;
import org.junit.Test;

/**
 * User: skarb
 * Date: 17/01/13
 */
public class XmlFileTest {

    @Test
    public void testretrieveApplication() {
        Assert.assertEquals("tyty", XmlFile.retrieveApplication("<root><log.pile.application>tyty</log.pile.application></root>"));
        Assert.assertEquals("tyty", XmlFile.retrieveApplication("<root>   <log.pile.application >tyty</log.pile.application></root>"));
        Assert.assertEquals("", XmlFile.retrieveApplication("<root>   <log.pile.application ></log.pile.application></root>"));
        Assert.assertNull(XmlFile.retrieveApplication("<root>   <log.pile.application.a >sss</log.pile.application.a></root>"));

    }

    @Test
    public void testretrieveEngine() {
        Assert.assertEquals("tyty", XmlFile.retrieveEngine("<root><log.pile.engine>tyty</log.pile.engine></root>"));
        Assert.assertEquals("tyty", XmlFile.retrieveEngine("<root>   <log.pile.engine >tyty </log.pile.engine></root>"));
        Assert.assertEquals("", XmlFile.retrieveEngine("<root>   <log.pile.engine></log.pile.engine></root>"));
        Assert.assertNull(XmlFile.retrieveEngine("<root>   <log.pile.application.a >sss</log.pile.application.a></root>"));

    }

    @Test
    public void testretrieveUrl() {
        Assert.assertEquals("tyty", XmlFile.retrieveUrl("<root><log.pile.url>tyty</log.pile.url></root>"));
        Assert.assertEquals("tyty", XmlFile.retrieveUrl("<root>   <log.pile.url >tyty</log.pile.url></root>"));
        Assert.assertEquals("", XmlFile.retrieveUrl("<root>   <log.pile.url></log.pile.url></root>"));
        Assert.assertNull(XmlFile.retrieveUrl("<root>   <log.pile.url.a >sss</log.pile.application.a></root>"));

    }

    @Test
    public void testFile(){
        final XmlFile xmlFile = new XmlFile("/test.xml");
        Assert.assertTrue(xmlFile.configured());
        Assert.assertEquals("APP1",xmlFile.getApplication());
        Assert.assertEquals("engine.class",xmlFile.getEngineClass());
        Assert.assertEquals("http://www.gogole.fr",xmlFile.getUrl());
    }
}
