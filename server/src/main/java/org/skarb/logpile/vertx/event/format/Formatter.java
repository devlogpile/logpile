package org.skarb.logpile.vertx.event.format;

import org.skarb.logpile.vertx.event.Event;

import java.util.Objects;

/**
 * User: skarb
 * Date: 10/01/13
 */
public class Formatter {

    private String separator;
    private String formatDate;

    protected Formatter(String separator, String formatDate) {
        this.separator = separator;
        this.formatDate = formatDate;
    }

    protected void setSeparator(final String separator) {
        this.separator = Objects.toString(separator, FormatterUtils.DEFAULT_SEPARATOR);
    }

    protected void setFormatDate(String formatDate) {
        this.formatDate = Objects.toString(formatDate, FormatterUtils.DEFAULT_FORMAT);
    }

    public String format(final Event event) {
        return FormatterUtils.format(separator, formatDate, event).toString();
    }

    public static class Builder {

        private Formatter formatter;


        private Builder() {
            formatter = new Formatter(FormatterUtils.DEFAULT_SEPARATOR, FormatterUtils.DEFAULT_FORMAT);
        }

        public Builder defaultValues()

        {
            formatter = new Formatter(FormatterUtils.DEFAULT_SEPARATOR, FormatterUtils.DEFAULT_FORMAT);
            return this;
        }

        public static Builder init() {
            return new Builder();
        }

        public Builder addSeparator(final String separator) {
            formatter.setSeparator(separator);
            return this;
        }

        public Builder addFormatDate(final String format) {
            formatter.setFormatDate(format);
            return this;
        }

        public Formatter build() {
            return formatter;
        }
    }
}
