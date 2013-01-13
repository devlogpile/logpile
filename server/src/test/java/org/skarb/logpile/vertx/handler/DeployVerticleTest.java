package org.skarb.logpile.vertx.handler;

import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.deploy.Container;

import static org.mockito.Mockito.*;

/**
 * User: skarb
 * Date: 03/01/13
 */
public class DeployVerticleTest {

    private static final MockingProgress mockingProgress = new ThreadSafeMockingProgress();

    private static String notcontains(final String substring) {
        return mockingProgress.getArgumentMatcherStorage().reportMatcher(new ArgumentMatcher<String>() {
            public boolean matches(Object actual) {
                return actual == null || !((String) actual).contains(substring);
            }
        }).returnString();
    }

    private static class A {}

    @Test
    public void testhandle() throws Exception {
        final Container container = mock(Container.class);
        final Logger logger = mock(Logger.class);
        when(container.getLogger()).thenReturn(logger);

        doThrow(new RuntimeException("bad call")).when(logger).info(notcontains("test"));

        final DeployVerticle deployVerticle = new DeployVerticle(container, A.class);
        deployVerticle.handle("test");

    }
}
