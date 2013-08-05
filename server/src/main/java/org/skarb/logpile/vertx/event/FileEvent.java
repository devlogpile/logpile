package org.skarb.logpile.vertx.event;

import org.skarb.logpile.vertx.event.format.FormatterUtils;
import org.skarb.logpile.vertx.event.format.LineFormatter;
import org.skarb.logpile.vertx.handler.HandlerUtils;
import org.skarb.logpile.vertx.utils.Charsets;
import org.skarb.logpile.vertx.utils.FileSizeUtils;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.AsyncResultHandler;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.file.FileProps;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.core.logging.impl.LoggerFactory;
import org.vertx.java.platform.Container;

import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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
    /**
     * Path to the log file.
     */
    String path;
    /**
     * Number of current rolling files.
     */
    int numberFile = -1;
    /**
     * prefixe Name of the file.
     */
    String name;
    /**
     * Format date for the file
     */
    String formattedDate;
    /**
     * rolling option in bytes.
     * <p>if the value is equal to -1, then the rolling option is desactivated.</p>
     */
    long rolling;
    /**
     * rolling option in configuration file.
     */
    String rollingField;
    /**
     * Line formatting option.
     */
    LineFormatter lineFormatter;

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDatas(final Vertx vertx, final Container container) {
        super.setDatas(vertx, container);
        final JsonObject objectConfig = getJsonObject();

        lineFormatter = LineFormatter.Builder.init().build();
        name = Objects.toString(objectConfig.getString(FILE_NAME_FIELD), DEFAULT_NAME);
        formattedDate = FormatterUtils.formatDate(FormatterUtils.LOG_FORMAT, new Date());
        rollingField = Objects.toString(objectConfig.getString(ROLLING_FIELD), null);
        rolling = FileSizeUtils.calculate(rollingField, DEFAULT_ROLLING);
        path = Objects.toString(objectConfig.getString(PATH_FIELD), DEFAULT_PATH);

        final String completePath = createPath();


        getVertx().fileSystem().createFile(completePath, HandlerUtils.logCreationFile(container, completePath));
    }

    /**
     * Calculate the dir path.
     *
     * @return the currentdir path.
     */
    private StringBuilder createDirPath() {
        final StringBuilder stringBuilder = new StringBuilder(path);
        if (stringBuilder.length() > 0 && stringBuilder.charAt(stringBuilder.length() - 1) != '/') {

            stringBuilder.append("/");
        }
        return stringBuilder;
    }

    /**
     * create the complete path to the current file.
     *
     * @return the complete path.
     */
    String createPath() {
        final StringBuilder stringBuilder = createDirPath();
        return stringBuilder.append(name).append("-").append(formattedDate).append(".log").toString();
    }

    /**
     * create the complete path for the next rolling file.
     *
     * @return the complete path
     */
    String createNextBackupPath() {
        final StringBuilder completePath = createDirPath();
        completePath.append(name).append("-").append(formattedDate);
        numberFile++;
        completePath.append(".").append(numberFile).append(".log");
        return completePath.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean handle(final Event event) {

        final String newLine = lineFormatter.format(event) + "\n";
        final int length = newLine.getBytes(Charsets.getDefault()).length;
        final String completePath = createPath();
        if (rolling != DEFAULT_ROLLING) {
            testIfRolling(completePath, newLine, length);
        } else {
            appendToFile(completePath, newLine);
        }
        return true;
    }

    /**
     * Method which manage the rolling option.
     *
     * @param completePath path to the file
     * @param datas        data to write
     * @param length       lenght of the datas.
     */
    private void testIfRolling(final String completePath, final String datas, final int length) {
        getVertx().fileSystem().props(completePath, new Handler<AsyncResult<FileProps>>() {
            @Override
            public void handle(final AsyncResult<FileProps> asyncResult) {
                long totalSpace = 0L;
                if (asyncResult.succeeded()) {
                    final FileProps fsProps = asyncResult.result();
                    totalSpace = fsProps.size();
                }

                if ((totalSpace + length) > rolling) {
                    final String backupFile = createNextBackupPath();
                    getVertx().fileSystem().copy(completePath, backupFile, doRolling(completePath, datas, backupFile));
                } else {
                    appendToFile(completePath, datas);
                }
            }
        });

    }

    /**
     * @param completePath path to the file
     * @param datas        data to write
     * @param backupFile   lenght of the datas.
     * @return the handler.
     */
    AsyncResultHandler<Void> doRolling(final String completePath, final String datas, final String backupFile) {
        return new AsyncResultHandler<Void>() {
            @Override
            public void handle(AsyncResult<Void> event) {
                log.info(new StringBuilder("creation of the backup file :").append(backupFile));
                getVertx().fileSystem().deleteSync(completePath);
                createFile(completePath, datas);
            }
        };
    }

    /**
     * Method for writing to a file.
     *
     * @param completePath the path of the file.
     * @param datas        the datas to write.
     */
    private void createFile(final String completePath, final String datas) {
        write(completePath, datas, StandardOpenOption.CREATE);
    }

    /**
     * Method for appending to a file.
     *
     * @param completePath the path of the file.
     * @param datas        the datas to write.
     */
    private void appendToFile(final String completePath, final String datas) {
        write(completePath, datas, StandardOpenOption.APPEND);
    }

    /**
     * Write to a file
     *
     * @param completePath the path of the file.
     * @param datas        the datas to write.
     * @param openOption   The option of writing.
     */
    private void write(final String completePath, final String datas, final StandardOpenOption openOption) {
        try {
            final Buffer buffer = new Buffer(datas.getBytes(Charsets.getDefault()));
            if (StandardOpenOption.APPEND.equals(openOption)) {
                getVertx().fileSystem().readFile(completePath, new Handler<AsyncResult<Buffer>>() {
                    @Override
                    public void handle(AsyncResult<Buffer> asyncResult) {
                        final Buffer internalBuff = new Buffer();

                        if (asyncResult.succeeded()) {
                            internalBuff.appendBuffer(asyncResult.result());
                        }
                        internalBuff.appendBuffer(buffer);
                        getVertx().fileSystem().
                                writeFile(completePath, internalBuff, HandlerUtils.logCreationFile(getContainer(), completePath));
                    }
                });
            } else {

                getVertx().fileSystem().
                        writeFile(completePath, buffer, HandlerUtils.logCreationFile(getContainer(), completePath));
            }


        } catch (Exception ex) {
            log.error("Error in writing file ", ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String describe() {
        final boolean rollingOption = rolling != DEFAULT_ROLLING;
        final StringBuilder message = new StringBuilder("Export all the Errors in a single file.<br>");

        final String absolutePath = Paths.get(createPath()).toAbsolutePath().toString();
        message.append("Path : ").append(absolutePath).append("<br>");
        message.append("Rolling option : ").append(rollingOption).append("<br>");
        if (rollingOption) {
            message.append("Size max : ").append(rollingField).append("<br>");
            message.append("Number of rolling files created : ").append(numberFile + 1).append("<br>");
        }
        return message.toString();
    }


}
