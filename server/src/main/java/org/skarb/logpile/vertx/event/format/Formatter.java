package org.skarb.logpile.vertx.event.format;

import org.skarb.logpile.vertx.event.Event;

import java.util.Objects;

/**
 * Formatter for the events.
 * <p>Transform the EVent in string line with a defined format.</p>
 * User: skarb
 * Date: 10/01/13
 */
public class Formatter {
    /**
     * The separator between the fields.
     */
    private String separator;
    /**
     * the date formatter.
     */
    private String formatDate;

    /**
     * Constructor.
     *
     * @param separator the separator.
     * @param formatDate the date format.
     */
    protected Formatter(String separator, String formatDate) {
        this.separator = separator;
        this.formatDate = formatDate;
    }

    /**
     * Set new Separator.
     *
     * @param separator the new value.
     */
    protected void setSeparator(final String separator) {
        this.separator = Objects.toString(separator, FormatterUtils.DEFAULT_SEPARATOR);
    }

    protected void setFormatDate(String formatDate) {
        this.formatDate = Objects.toString(formatDate, FormatterUtils.DEFAULT_FORMAT);
    }

    public String format(final Event event) {
        return FormatterUtils.format(separator, formatDate, event).toString();
    }

    /**
     * Builder for create a new instance of formatter.
     */
    public static class Builder {
        /**
         * The current instance in build.
         */
        private Formatter formatter;

        /**
         * a new instance.
         */
        private Builder() {
            formatter = new Formatter(FormatterUtils.DEFAULT_SEPARATOR, FormatterUtils.DEFAULT_FORMAT);
        }

        /**
         * CReate e new instance.
         *
         * @return
         */
        public static Builder init() {
            return new Builder();
        }

        /**
         * reinit to initial value.
         *
         * @return the builder.
         */
        public Builder defaultValues()

        {
            formatter = new Formatter(FormatterUtils.DEFAULT_SEPARATOR, FormatterUtils.DEFAULT_FORMAT);
            return this;
        }

        /**
         * change the separator.
         * @param separator  the ne value.
         * @return the current object
         */
        public Builder changeSeparator(final String separator) {
            formatter.setSeparator(separator);
            return this;
        }

        /**
         * change the format date.
         * @param format the new format.
         * @return the current object
         */
        public Builder changeFormatDate(final String format) {
            formatter.setFormatDate(format);
            return this;
        }

        /**
         * Build the instance.
         *
         * @return a new instance.
         */
        public Formatter build() {
            return formatter;
        }
    }
}
