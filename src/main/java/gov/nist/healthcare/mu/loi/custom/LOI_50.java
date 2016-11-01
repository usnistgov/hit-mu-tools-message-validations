package gov.nist.healthcare.mu.loi.custom;

import gov.nist.healthcare.mu.loi.custom.util.DateUtil;
import gov.nist.healthcare.mu.loi.custom.util.ExtendedDate;
import hl7.v2.instance.Query;
import hl7.v2.instance.Simple;
import scala.collection.immutable.List;

public class LOI_50 {

    /*
     * LOI-50: If present, OBR-8 (Observation End Date/Time) SHALL be equal to
     * or later than OBR-7 (Observation Date/Time).
     */

    /**
     * @param e
     *        OBR context
     * @return true if OBR-8 or OBR-7 is not present, true if OBR-8 is equal or
     *         later than OBR-7, false otherwise
     */
    public boolean assertion(hl7.v2.instance.Element e) {
        List<Simple> OBR7List = Query.queryAsSimple(e, "7[1].1[1]").get();
        if (OBR7List == null || OBR7List.size() == 0) {
            // OBR-7 is not present
            return true;
        }
        List<Simple> OBR8List = Query.queryAsSimple(e, "8[1].1[1]").get();
        if (OBR8List == null || OBR8List.size() == 0) {
            // OBR-8 is not present
            return true;
        }

        String OBR7 = OBR7List.apply(0).value().raw();
        String OBR8 = OBR8List.apply(0).value().raw();

        // check that we can compare
        if (!DateUtil.canCompare(OBR7, OBR8)) {
            return false;
        }
        // compare the dates
        ExtendedDate d1 = DateUtil.toExtendedDate(OBR7);
        ExtendedDate d2 = DateUtil.toExtendedDate(OBR8);
        if (!d1.isEqual(d2) && d2.isBefore(d1)) {
            return false;
        }
        return true;
    }

}
