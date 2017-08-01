package gov.nist.healthcare.mu.lri.custom;

import gov.nist.healthcare.mu.lri.custom.util.DateComparator;
import gov.nist.healthcare.mu.lri.custom.util.DateUtil;
import gov.nist.healthcare.mu.lri.custom.util.ExtendedDate;
import hl7.v2.instance.Query;
import hl7.v2.instance.Simple;
import java.util.ArrayList;
import java.util.Collections;
import scala.collection.Iterator;
import scala.collection.immutable.List;

public class LRI_PH_100 {

    /*
     * LRI-PH-100: The earliest SPM-17.1 (Range Start Date/Time) value SHALL be
     * equal to or before OBR-7 (Observation Date/Time) value within the same
     * ORDER_OBSERVATION Group.
     */

    /**
     * @param e
     *        message context : ORDER_OBSERVATION Group
     * @return true if assertion is verified
     */
    public boolean assertion(hl7.v2.instance.Element e) {

        // OBR-7
        List<Simple> OBR7List = Query.queryAsSimple(e, "2[1].7[1].1[1]").get();

        // SPM-17.1 List
        List<Simple> SPM_17_1List = Query.queryAsSimple(e,
                "9[*].1[1].17[1].1[1].1[1]").get();

        Iterator<Simple> it = SPM_17_1List.iterator();
        ArrayList<ExtendedDate> dates = new ArrayList<ExtendedDate>();

        if (OBR7List.size() == 0 && SPM_17_1List.size() != 0) {
            // can't compare because OBR-7 is missing
            return false;
        }

        Simple OBR7 = getSimple(OBR7List);

        while (it.hasNext()) {
            Simple SPM17_1 = it.next();
            if (!DateUtil.canCompare(OBR7.value().raw(), SPM17_1.value().raw())) {
                // can't compare
                return false;
            }
            dates.add(DateUtil.toExtendedDate(SPM17_1.value().raw()));
        }

        ExtendedDate earliest = Collections.min(dates, new DateComparator());
        ExtendedDate OBR7Date = DateUtil.toExtendedDate(OBR7.value().raw());

        return (earliest.isEqual(OBR7Date) || earliest.isAfter(OBR7Date));

    }

    protected Simple getSimple(List<Simple> simpleElementList) {
        if (simpleElementList.size() > 1) {
            throw new IllegalArgumentException("Invalid List size : "
                    + simpleElementList.size());
        }
        if (simpleElementList.size() == 0) {
            return null;
        }
        // only get first element
        return simpleElementList.apply(0);
    }
}
