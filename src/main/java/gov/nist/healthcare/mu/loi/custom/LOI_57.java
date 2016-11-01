package gov.nist.healthcare.mu.loi.custom;

import hl7.v2.instance.Element;
import hl7.v2.instance.Query;
import hl7.v2.instance.Simple;
import scala.collection.Iterator;
import scala.collection.immutable.List;

public class LOI_57 {

    /*
     * For each value in OBR-28 (Result Copies To) a corresponding PRT
     * (Participant Information) SHALL be present with PRT-4.1 (Participation
     * Identifier) valued ‘RCT’.
     */

    /**
     * @param e
     *        OBSERVATION_REQUEST context
     * @return false if the message contains more OBR-28 than PRT where
     *         PRT-4.1='RCT' (within the same OBSERVATION_REQUEST)
     */
    public boolean assertion(hl7.v2.instance.Element e) {
        // this conformance statement only applies to "new and append order"
        int ct = 0;
        List<Element> OBR_28List = Query.query(e, "1[1].28[*]").get();
        List<Simple> PRTList = Query.queryAsSimple(e, "4[*].4[1].1[1]").get();
        if (PRTList.size() > 0) {
            Iterator<Simple> it = PRTList.iterator();
            // count the PRTs where PRT-4.1 = 'RCT'
            while (it.hasNext()) {
                Simple next = it.next();
                String value = next.value().raw();
                if ("RCT".equals(value)) {
                    ct++;
                }
            }
        }
        return ct >= OBR_28List.size();
    }
}
