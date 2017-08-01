package gov.nist.healthcare.mu.lri.custom;

import hl7.v2.instance.Query;
import hl7.v2.instance.Simple;
import java.util.HashSet;
import java.util.Set;
import scala.collection.Iterator;
import scala.collection.immutable.List;

public class LRI_PH_96 {

    /*
     * LRI-PH-96: OBX-14 (Date/Time of the Observation) For observation related
     * to testing of specimen (OBX's following the OBR), SHALL be identical to
     * an occurrence of SPM-17.1 (Range Start Date/Time) value within the same
     * ORDER_OBSERVATION Group.
     */

    /**
     * @param e
     *        message context : ORDER_OBSERVATION Group
     * @return true if assertion is verified
     */
    public boolean assertion(hl7.v2.instance.Element e) {

        // OBX-14 values
        List<Simple> OBX_14List = Query.queryAsSimple(e, "6[*].1[1].14[1].1[1]").get();
        if (OBX_14List.size() > 0) {
            Set<String> OBX_14Set = new HashSet<String>();
            Iterator<Simple> it = OBX_14List.iterator();
            while (it.hasNext()) {
                Simple next = it.next();
                String value = next.value().raw();
                OBX_14Set.add(value);
            }

            // SPM-17.1 values
            List<Simple> SPM_17_1List = Query.queryAsSimple(e,
                    "9[*].1[1].17[1].1[1].1[1]").get();
            Set<String> SPM_17_1Set = new HashSet<String>();
            Iterator<Simple> it2 = SPM_17_1List.iterator();
            while (it2.hasNext()) {
                Simple next = it2.next();
                String value = next.value().raw();
                SPM_17_1Set.add(value);
            }
            return SPM_17_1Set.containsAll(OBX_14Set);
        }
        return true;
    }
}
