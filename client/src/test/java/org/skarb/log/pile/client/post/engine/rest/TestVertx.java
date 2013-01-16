package org.skarb.log.pile.client.post.engine.rest;

import org.skarb.log.pile.client.Event;
import org.skarb.log.pile.client.util.ClientConstantes;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Date;

import static org.junit.Assert.assertTrue;

public class TestVertx {

    public static void main(String[] args)  throws Exception {
        TestVertx testVertx = new TestVertx();
        testVertx.postGood();
    }

   // @Test
    public void postGood() throws Exception {

        System.setProperty(ClientConstantes.PROPERTIES_APPLICATION, "truc");
        System.setProperty(ClientConstantes.PROPERTIES_URL_REST, "http://localhost:8082/event");
        EngineRestGet engineRest = new EngineRestGet();
        final Event createInstance = newInstance();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new NullPointerException().printStackTrace(new PrintStream(out));


        createInstance.setStacktrace(new String(out.toByteArray())
        );
        engineRest.post(createInstance);


        assertTrue(true);

    }

   // @Test
    public void postGoodWithStackTrace() throws Exception {
        for (int i = 0; i < 25; i++) {


            System.setProperty(ClientConstantes.PROPERTIES_APPLICATION, "truc");
            System.setProperty(ClientConstantes.PROPERTIES_URL_REST, "http://localhost:8082/event");
            EngineRestGet engineRest = new EngineRestGet();
            final Event createInstance = newInstance();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            new NullPointerException().printStackTrace(new PrintStream(out));


            createInstance.setStacktrace(new String(out.toByteArray())
            );

            engineRest.post(createInstance);

            Event newInstance = newInstance();
            newInstance.setMessage("Test de très très long message");
            engineRest.post(newInstance);

            assertTrue(true);
        }

    }

    private Event newInstance() {
        final Event createInstance = new Event();
        createInstance.setApplication("App1");
        createInstance.setDate(new Date());
        createInstance.setComponent("a");
        createInstance.setMessage("Test message exception");
        return createInstance;
    }

}
