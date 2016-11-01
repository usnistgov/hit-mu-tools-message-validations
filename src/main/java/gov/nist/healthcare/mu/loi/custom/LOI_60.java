package gov.nist.healthcare.mu.loi.custom;

import hl7.v2.instance.Query;
import hl7.v2.instance.Simple;
import scala.collection.Iterator;
import scala.collection.immutable.List;

public class LOI_60 {

    /*
     * LOI-60: Only one instance of DG1-15 (Diagnosis Priority) in the message
     * SHALL contain the value '1'.
     */

    /**
     * @param e
     *        the message context
     * @return false if the message contains more that one DG1 where DG1-15
     *         ='1', true otherwise
     */
    public boolean assertion(hl7.v2.instance.Element e) {
        // this conformance statement only applies to "new and append order"
        // 5[*].3[1].6[*].15[1] = ORDER[*].OBSERVATION_REQUEST[1].DG1[*].15[1]
        List<Simple> DG1_15List = Query.queryAsSimple(e, "6[*].15[1]").get();
        Iterator<Simple> it = DG1_15List.iterator();
        int ct = 0;
        while (it.hasNext()) {
            Simple next = it.next();
            String value = next.value().raw();
            if ("1".equals(value)) {
                ct++;
            }
        }
        return ct <= 1;
    }
}
