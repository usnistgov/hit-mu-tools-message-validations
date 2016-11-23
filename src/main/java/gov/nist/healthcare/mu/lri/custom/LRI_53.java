package gov.nist.healthcare.mu.lri.custom;

import gov.nist.healthcare.mu.lri.custom.util.DateUtil;
import gov.nist.healthcare.mu.lri.custom.util.ExtendedDate;
import hl7.v2.instance.Element;
import hl7.v2.instance.Query;
import hl7.v2.instance.Simple;
import scala.collection.Iterator;
import scala.collection.immutable.List;

public class LRI_53 {

    /*
     * If one or more SPM segments are present for the same OBR, then the
     * earliest SPM-17.1 (Range Start Date/Time) SHALL be equal to or before
     * OBR-7 (Observation Date/Time) and OBR-7 (Observation Date/Time) SHALL be
     * equal to or before the latest SPM-17.2 (Range End Date/Time).
     */

    /**
     * @param e
     *        ORDER_OBSERVATION context
     * @return true if assertion is verified
     */
    public boolean assertion(hl7.v2.instance.Element e) {
        List<Element> SPM17List = Query.query(e, "9[*].1[1].17[1]").get();
        if (SPM17List.length() == 0) {
            // there is not SPM-17
            return true;
        }

        List<Simple> SPM17_1List = Query.queryAsSimple(e,
                "9[*].1[1].17[1].1[1].1[1]").get();
        List<Simple> SPM17_2List = Query.queryAsSimple(e,
                "9[*].1[1].17[1].2[1].1[1]").get();

        List<Simple> OBR7_1List = Query.queryAsSimple(e, "2[1].7[1].1[1]").get();
        ExtendedDate OBR7_1 = DateUtil.toExtendedDate(getValue(OBR7_1List));

        // get the SPM-17.1 minimum
        ExtendedDate SPM17_1Min = null;
        Iterator<Simple> it1 = SPM17_1List.iterator();
        while (it1.hasNext()) {
            Simple next = it1.next();
            String value = next.value().raw();
            if (SPM17_1Min == null) {
                SPM17_1Min = DateUtil.toExtendedDate(value);
            } else {
                ExtendedDate d1 = DateUtil.toExtendedDate(value);
                if (d1.isBefore(SPM17_1Min)) {
                    SPM17_1Min = d1;
                }
            }
        }

        // get the SPM-17.2 maximum
        ExtendedDate SPM17_2Max = null;
        Iterator<Simple> it2 = SPM17_2List.iterator();
        while (it2.hasNext()) {
            Simple next = it2.next();
            String value = next.value().raw();
            if (SPM17_2Max == null) {
                SPM17_2Max = DateUtil.toExtendedDate(value);
            } else {
                ExtendedDate d1 = DateUtil.toExtendedDate(value);
                if (d1.isAfter(SPM17_2Max)) {
                    SPM17_2Max = d1;
                }
            }
        }
        boolean b1 = false;
        boolean b2 = false;
        // compare OBR-7 and min(SPM-17.1)
        if (SPM17_1Min != null) {
            // System.err.println("SPM-17.1 min : " + SPM17_1Min);
            // System.err.println("OBR-7.1      : " + OBR7_1);
            b1 = (SPM17_1Min.isBefore(OBR7_1) || SPM17_1Min.isEqual(OBR7_1));
        } else {
            b1 = true;
        }
        // System.err.println(b1);
        // compare OBR-7 and max(SPM-17.2)
        if (SPM17_2Max != null) {
            b2 = (OBR7_1.isBefore(SPM17_2Max) || OBR7_1.isEqual(SPM17_2Max));
        } else {
            b2 = true;
        }
        return b1 && b2;
    }

    private String getValue(List<Simple> simpleElementList) {
        if (simpleElementList.size() > 1) {
            throw new IllegalArgumentException("Invalid List size : "
                    + simpleElementList.size());
        }
        if (simpleElementList.size() == 0) {
            return "";
        }
        // only get first element
        return simpleElementList.apply(0).value().raw();
    }
}
