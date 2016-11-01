package gov.nist.healthcare.mu.loi.custom.util;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

public class ExtendedDate {

    private DateTime jodaDate;
    private int millis;

    public ExtendedDate(DateTime jodaDate, int millis) {
        this.jodaDate = jodaDate;
        this.millis = millis;
    }

    public DateTime getJodaDate() {
        return jodaDate;
    }

    public int getMillis() {
        return millis;
    }

    public boolean isBefore(ExtendedDate date) {
        boolean jDateBefore = this.jodaDate.isBefore(date.getJodaDate());
        boolean millisBefore = this.millis > date.getMillis();
        if (this.jodaDate.isEqual(date.getJodaDate())) {
            return millisBefore;
        } else {
            return jDateBefore;
        }
    }

    public boolean isAfter(ExtendedDate date) {
        boolean jDateAfter = this.jodaDate.isAfter(date.getJodaDate());
        boolean millisAfter = this.millis > date.getMillis();
        if (this.jodaDate.isEqual(date.getJodaDate())) {
            return millisAfter;
        } else {
            return jDateAfter;
        }
    }

    public boolean isEqual(ExtendedDate date) {
        boolean jDateEqual = this.jodaDate.isEqual(date.getJodaDate());
        boolean millisEqual = this.millis == date.getMillis();
        return jDateEqual && millisEqual;
    }

    public String toString() {
        String m = StringUtils.rightPad(String.valueOf(millis), 4, "0");
        String toString = jodaDate.toString().replaceFirst("\\.\\d{3}", "." + m);
        return toString;
    }

}
