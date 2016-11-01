package gov.nist.healthcare.mu.loi.custom;

import hl7.v2.instance.Element;
import hl7.v2.instance.Query;
import hl7.v2.instance.Simple;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import scala.collection.Iterator;
import scala.collection.immutable.List;

public class LOI_ORL_53 {

    /*
     * LOI-53: The value of OBR-2 (Placer Order Number) SHALL NOT be valued
     * identical to another instance of OBR-2 (Placer Order Number) in the
     * message.
     */

    /**
     * @param e
     *        message context
     * @return true if assertion verified
     */
    public boolean assertion(hl7.v2.instance.Element e) {
        // (RESPONSE)6[1].(PATIENT)1[1].(ORDER)2[*].(OBSERVATION_REQUEST)3[1].(OBR)1[1].(OBR-2)2[1]

        List<Element> OBR2List = Query.query(e,
                " 6[1].1[1].2[*].3[1].1[1].2[1]").get();
        Iterator<Element> it = OBR2List.iterator();
        Set<java.util.List<String>> values = new HashSet<java.util.List<String>>();
        while (it.hasNext()) {
            Element next = it.next();

            List<Simple> OBR2_1List = Query.queryAsSimple(next, "1[1]").get();
            List<Simple> OBR2_2List = Query.queryAsSimple(next, "2[1]").get();
            List<Simple> OBR2_3List = Query.queryAsSimple(next, "3[1]").get();
            List<Simple> OBR2_4List = Query.queryAsSimple(next, "4[1]").get();

            String OBR2_1 = OBR2_1List.size() > 0 ? OBR2_1List.apply(0).value().raw()
                    : "";
            String OBR2_2 = OBR2_2List.size() > 0 ? OBR2_2List.apply(0).value().raw()
                    : "";
            String OBR2_3 = OBR2_3List.size() > 0 ? OBR2_3List.apply(0).value().raw()
                    : "";
            String OBR2_4 = OBR2_4List.size() > 0 ? OBR2_4List.apply(0).value().raw()
                    : "";

            java.util.List<String> OBR2 = Arrays.asList(OBR2_1, OBR2_2, OBR2_3,
                    OBR2_4);

            if (values.contains(OBR2)) {
                return false;
            }
            values.add(OBR2);
        }
        return true;
    }
}
