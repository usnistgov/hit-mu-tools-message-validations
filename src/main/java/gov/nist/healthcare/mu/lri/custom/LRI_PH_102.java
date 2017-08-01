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

public class LRI_PH_102 {

    /*
     * LRI-PH-102: If present, the latest SPM-17.2 (Range End Date/Time) value
     * SHALL be equal to or after OBR-8 (Observation End Date/Time) value within
     * the same ORDER_OBSERVATION Group
     */

    /**
     * @param e
     *        message context : ORDER_OBSERVATION Group
     * @return true if assertion is verified
     */
    public boolean assertion(hl7.v2.instance.Element e) {

        // OBR-8
        List<Simple> OBR8List = Query.queryAsSimple(e, "2[1].8[1].1[1]").get();

        // SPM-17.2 List
        List<Simple> SPM_17_2List = Query.queryAsSimple(e,
                "9[*].1[1].17[1].2[1].1[1]").get();

        Iterator<Simple> it = SPM_17_2List.iterator();
        ArrayList<ExtendedDate> dates = new ArrayList<ExtendedDate>();

        if (OBR8List.size() == 0 && SPM_17_2List.size() != 0) {
            // can't compare because OBR-8 is missing
            return false;
        }

        Simple OBR8 = getSimple(OBR8List);

        while (it.hasNext()) {
            Simple SPM17_2 = it.next();
            if (!DateUtil.canCompare(OBR8.value().raw(), SPM17_2.value().raw())) {
                // can't compare
                return false;
            }
            dates.add(DateUtil.toExtendedDate(SPM17_2.value().raw()));
        }

        ExtendedDate latest = Collections.max(dates, new DateComparator());
        ExtendedDate OBR8Date = DateUtil.toExtendedDate(OBR8.value().raw());

        return (latest.isEqual(OBR8Date) || latest.isAfter(OBR8Date));

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
