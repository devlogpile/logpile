package org.skarb.logpile.vertx.event;


import org.junit.Test;
import org.skarb.logpile.vertx.event.format.Formatter;
import org.skarb.logpile.vertx.utils.Charsets;
import org.vertx.java.core.AsyncResultHandler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.file.FileSystem;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.deploy.Container;

import java.io.File;
import java.net.URL;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * User: skarb
 * Date: 15/01/13
 */
public class FileEventTest {

    @Test
    public void testcreatePath() {
        final FileEvent fileEvent = new FileEvent();
        fileEvent.path = FileEvent.DEFAULT_PATH;
        fileEvent.name = "test";
        fileEvent.formattedDate = "2012-12-12";

        assertEquals("test-2012-12-12.log", fileEvent.createPath());

        fileEvent.path = "t/a";
        assertEquals("t/a/test-2012-12-12.log", fileEvent.createPath());
        fileEvent.path = "t/c/";
        assertEquals("t/c/test-2012-12-12.log", fileEvent.createPath());

    }

    @Test
    public void testcreateNextBackupPath() {
        final FileEvent fileEvent = new FileEvent();
        fileEvent.path = FileEvent.DEFAULT_PATH;
        fileEvent.name = "test";
        fileEvent.formattedDate = "2012-12-12";


        assertEquals("test-2012-12-12.0.log", fileEvent.createNextBackupPath());
        assertEquals("test-2012-12-12.1.log", fileEvent.createNextBackupPath());
        fileEvent.path = "j";
        assertEquals("j/test-2012-12-12.2.log", fileEvent.createNextBackupPath());
    }

    @Test
    public void testSetDatas() {
        final FileEvent fileEvent = new FileEvent();
        final Container container = mock(Container.class);
        when(container.getConfig()).thenReturn(new JsonObject());
        final Vertx vertx = mock(Vertx.class);
        when(vertx.fileSystem()).thenReturn(mock(FileSystem.class));
        fileEvent.setDatas(vertx, container);
        verify(vertx.fileSystem()).createFile(anyString(), (AsyncResultHandler<Void>) any());
        assertNotNull(fileEvent.describe());
    }

    private String createTmpPath() throws Exception {
        URL resource = this.getClass().getClassLoader().getResource("tmp.txt");
        final File file = new File(resource.toURI());
        String absolutePath = file.getParentFile().getAbsolutePath();

        return absolutePath;
    }

    private void reinit(FileEvent fileEvent) throws Exception {
        final String path = fileEvent.createPath();
        final Path pPath = new File(path).toPath();
        if (Files.exists(pPath)) {
            Files.delete(pPath);
        }
        Files.createFile(pPath);
    }

    private void setDatas(FileEvent fileEvent) throws Exception {
        final String path = fileEvent.createPath();
        final Path pPath = new File(path).toPath();
        Files.write(pPath, "qdsq dqsdqsds dsqd dsqsd sdsqd sdsqdqs sdqsdqsd sqdqsdqsd dsqsdsqdqs dqsdqsdqsd sddsqdsq sdqdsqdqs dsqdqsdsq sdsqqsdsqdqs dqsdqsdsq".getBytes());
    }

    @Test
    public void testhandleWithoutRolling() throws Exception {
        final FileEvent fileEvent = new FileEvent();
        final Event evt = Event.Builder(new JsonObject().putString(Event.PROP_APPLICATION, "APP").putString(Event.PROP_MESSAGE, "message !!"));
        fileEvent.name = "test";
        fileEvent.formattedDate = "2001-01-01";
        fileEvent.path = createTmpPath();
        fileEvent.rolling = FileEvent.DEFAULT_ROLLING;
        fileEvent.formatter = Formatter.Builder.init().build();

        reinit(fileEvent);

        fileEvent.handle(evt);

        final String completePath = fileEvent.createPath();
        final Path pPath = new File(completePath).toPath();
        final List<String> lines = Files.readAllLines(pPath, Charsets.getDefault());
        assertTrue(lines.get(lines.size()-1).contains("APP"));
        assertTrue(lines.get(lines.size()-1).contains("message !!"));
    }


    @Test
     public void testhandleWithRollingNoLimit() throws Exception {
        final FileEvent fileEvent = new FileEvent();
        final Event evt = Event.Builder(new JsonObject().putString(Event.PROP_APPLICATION, "APP").putString(Event.PROP_MESSAGE, "message !!"));

        final Container container = mock(Container.class);
        when(container.getConfig()).thenReturn(new JsonObject());
        final Vertx vertx = mock(Vertx.class);
        when(vertx.fileSystem()).thenReturn(mock(FileSystem.class));
        fileEvent.setDatas(vertx, container);

        fileEvent.name = "test";
        fileEvent.formattedDate = "2001-01-01";
        fileEvent.path = createTmpPath();
        fileEvent.rolling = 10000L;
        fileEvent.formatter = Formatter.Builder.init().build();

        reinit(fileEvent);

        fileEvent.handle(evt);

        final String completePath = fileEvent.createPath();
        final Path pPath = new File(completePath).toPath();
        final List<String> lines = Files.readAllLines(pPath,Charsets.getDefault());
        assertTrue(lines.get(lines.size()-1).contains("APP"));
        assertTrue(lines.get(lines.size()-1).contains("message !!"));

        verify(vertx.fileSystem(),never()).copy(anyString(),anyString(),any(AsyncResultHandler.class));
    }

    @Test
    public void testlogCreationFile() throws Exception {
        final FileEvent fileEvent = new FileEvent();
        final AsyncResultHandler<Void> result = fileEvent.logCreationFile("TEST");
        assertNotNull(result);
        result.handle(null);
    }

    @Test
    public void testhandleWithRolling() throws Exception {
        final FileEvent fileEvent = new FileEvent();
        final Event evt = Event.Builder(new JsonObject().putString(Event.PROP_APPLICATION, "APP").putString(Event.PROP_MESSAGE, "message !!"));

        final Container container = mock(Container.class);
        when(container.getConfig()).thenReturn(new JsonObject());
        final Vertx vertx = mock(Vertx.class);
        when(vertx.fileSystem()).thenReturn(mock(FileSystem.class));
        fileEvent.setDatas(vertx, container);

        fileEvent.name = "test";
        fileEvent.formattedDate = "2001-01-01";
        fileEvent.path = createTmpPath();
        fileEvent.rolling = 40L;
        fileEvent.formatter = Formatter.Builder.init().build();

        reinit(fileEvent);
        setDatas(fileEvent);

        fileEvent.handle(evt);

        verify(vertx.fileSystem(),atLeastOnce()).copy(anyString(), anyString(), any(AsyncResultHandler.class));
    }

    @Test
    public void testdoRolling() throws Exception {
        final FileEvent fileEvent = new FileEvent();
        File successFile = File.createTempFile("success", "sss");
        File oldFile = File.createTempFile("old", "sss");

        AsyncResultHandler<Void> result = fileEvent.doRolling(successFile.getAbsolutePath(), "newLine", oldFile.toPath(), "oldfileName");
        assertNotNull(result);
        result.handle(null);

        assertFalse(Files.exists(oldFile.toPath()));

        List<String> strings = Files.readAllLines(successFile.toPath(), Charsets.getDefault());
        assertEquals("newLine",strings.get(0));

    }
}
