package gov.nist.healthcare.mu.lri.custom.util;

import java.util.Comparator;

public class DateComparator implements Comparator<ExtendedDate> {

    @Override
    public int compare(ExtendedDate o1, ExtendedDate o2) {
        if (o1.isAfter(o2)) {
            return -1;
        }
        if (o1.isEqual(o2)) {
            return 0;
        }
        return 1;
    }

}
