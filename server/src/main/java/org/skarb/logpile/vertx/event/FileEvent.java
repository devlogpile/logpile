package org.skarb.logpile.vertx.event;

import org.skarb.logpile.vertx.event.format.Formatter;
import org.skarb.logpile.vertx.event.format.FormatterUtils;
import org.skarb.logpile.vertx.field.FileSizeUtils;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.AsyncResultHandler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.file.impl.PathAdjuster;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.core.logging.impl.LoggerFactory;
import org.vertx.java.deploy.Container;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.Objects;

/**
 * User: skarb
 * Date: 10/01/13
 */
public class FileEvent extends AbstractEventMessage {

    public static final String DEFAULT_NAME = "logpile";
    public static final String FILE_NAME_FIELD = "fileName";
    public static final String ROLLING_FIELD = "rolling";
    public static final long DEFAULT_ROLLING = -1L;
    public static final String PATH_FIELD = "path";
    public static final String DEFAULT_PATH = "";
    /**
     * Logger.
     */
    private static final Logger log = LoggerFactory.getLogger(FileEvent.class);
    String path;
    int numberFile = -1;
    String name;
    String formattedDate;
    long rolling;
    Formatter formatter;

    @Override
    public void setDatas(final Vertx vertx, final Container container) {
        super.setDatas(vertx, container);
        final JsonObject objectConfig = getJsonObject(container);

        formatter = Formatter.Builder.init().build();
        name = Objects.toString(objectConfig.getString(FILE_NAME_FIELD), DEFAULT_NAME);
        formattedDate = FormatterUtils.formatDate(FormatterUtils.LOG_FORMAT, new Date());
        rolling = FileSizeUtils.calculate(objectConfig.getString(ROLLING_FIELD), DEFAULT_ROLLING);
        path = Objects.toString(objectConfig.getString(PATH_FIELD), DEFAULT_PATH);

        final String completePath = createPath();


        getVertx().fileSystem().createFile(completePath, logCreationFile(completePath));
    }

    AsyncResultHandler<Void> logCreationFile(final String completePath) {
        return new AsyncResultHandler<Void>() {
            @Override
            public void handle(AsyncResult<Void> event) {
                log.info(new StringBuilder("the log file '").append(completePath).append("' is created."));
            }
        };
    }

    private StringBuilder createDirPath() {
        final StringBuilder stringBuilder = new StringBuilder(path);
        if (stringBuilder.length() > 0 && stringBuilder.charAt(stringBuilder.length() - 1) != '/') {

            stringBuilder.append("/");
        }
        return stringBuilder;
    }

    String createPath() {

        final StringBuilder stringBuilder = createDirPath();
        return stringBuilder.append(name).append("-").append(formattedDate).append(".log").toString();
    }

    String createNextBackupPath() {
        final StringBuilder completePath = createDirPath();
        completePath.append(name).append("-").append(formattedDate);
        numberFile++;
        completePath.append(".").append(numberFile).append(".log");
        return completePath.toString();
    }

    private JsonObject getJsonObject(Container container) {
        JsonObject objectConfig = container.getConfig().getObject(this.getClass().getName());
        if (objectConfig == null) {
            objectConfig = new JsonObject();
        }
        return objectConfig;
    }

    @Override
    public boolean handle(final Event event) {

        final String newLine = formatter.format(event) + "\n";
        final int length = newLine.getBytes().length;
        final String completePath = createPath();
        if (rolling != DEFAULT_ROLLING) {
            testIfRolling(completePath, newLine, length);
        } else {
            appendToFile(completePath, newLine);
        }
        return true;


    }

    private void testIfRolling(final String completePath, final String newLine, int length) {
        final Path target = PathAdjuster.adjust(Paths.get(completePath));
        long totalSpace = 0L;
        try {
            final BasicFileAttributes attrs = Files.readAttributes(target, BasicFileAttributes.class);
            totalSpace = attrs.size();
        } catch (Exception ex) {
            log.error("error in calculate space", ex);
        }
        if ((totalSpace + length) > rolling) {
            final String backupFile = createNextBackupPath();
            getVertx().fileSystem().copy(completePath, backupFile, doRolling(completePath, newLine, target, backupFile));

        } else {
            appendToFile(completePath, newLine);
        }
    }

    AsyncResultHandler<Void> doRolling(final String completePath, final String newLine, final Path target, final String backupFile) {
        return new AsyncResultHandler<Void>() {
            @Override
            public void handle(AsyncResult<Void> event) {
                log.info(new StringBuilder("creation of the backup file :").append(backupFile));
                try {
                    Files.delete(target);
                } catch (Exception ex) {
                    log.error("error in deleting file", ex);
                }
                createFile(completePath, newLine);
            }
        };
    }

    private void createFile(final String completePath, final String newLine) {
        write(completePath, newLine, StandardOpenOption.CREATE);
    }

    private void appendToFile(final String completePath, final String newLine) {
        write(completePath, newLine, StandardOpenOption.APPEND);
    }

    private void write(final String completePath, final String newLine, final StandardOpenOption openOption) {
        try {
            final Path target = PathAdjuster.adjust(Paths.get(completePath));
            Files.write(target, newLine.getBytes(), openOption);
        } catch (Exception ex) {
            log.error("Error in writing file ", ex);
        }
    }

    @Override
    public String describe() {
        return "File log.";
    }


}
