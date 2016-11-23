package gov.nist.healthcare.mu.lri.custom;

import gov.nist.healthcare.mu.lri.custom.util.DateUtil;
import gov.nist.healthcare.mu.lri.custom.util.ExtendedDate;
import hl7.v2.instance.Query;
import hl7.v2.instance.Simple;
import scala.collection.Iterator;
import scala.collection.immutable.List;

public class LRI_54 {

    /*
     * If one or more SPM segments are present for the same OBR and if OBR-8
     * (Observation End Date/Time) is present, OBR-8 (Observation End Date/Time)
     * SHALL be equal to or before the latest SPM-17.2 (Range End Date/Time).
     */

    /**
     * @param e
     *        ORDER_OBSERVATION context
     * @return true if assertion is verified
     */
    public boolean assertion(hl7.v2.instance.Element e) {

        List<Simple> SPM17_2List = Query.queryAsSimple(e,
                "9[*].1[1].17[1].2[1]").get();
        List<Simple> OBR8_1List = Query.queryAsSimple(e, "2[1].8[1].1[1]").get();
        if (OBR8_1List.length() == 0) {
            return true;
        }

        ExtendedDate OBR8_1 = DateUtil.toExtendedDate(getValue(OBR8_1List));

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
        boolean b2 = false;
        // compare OBR-8 and max(SPM-17.2)
        if (SPM17_2Max != null) {
            b2 = (OBR8_1.isBefore(SPM17_2Max) || OBR8_1.isEqual(SPM17_2Max));
        } else {
            b2 = true;
        }
        return b2;
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
