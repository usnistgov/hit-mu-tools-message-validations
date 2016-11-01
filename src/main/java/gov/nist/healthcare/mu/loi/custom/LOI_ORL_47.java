package gov.nist.healthcare.mu.loi.custom;

import hl7.v2.instance.Element;
import hl7.v2.instance.Query;
import hl7.v2.instance.Simple;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import scala.collection.Iterator;
import scala.collection.immutable.List;

public class LOI_ORL_47 {

    /*
     * LOI-47: The value of ORC-2 (Placer Order Number) SHALL NOT be valued
     * identical to another instance of ORC-2 (Placer Order Number) within the
     * same message excluding the Prior Result group(s).
     */

    /**
     * @param e
     *        message context
     * @return
     */
    public boolean assertion(hl7.v2.instance.Element e) {
        // (RESPONSE)6[1].(PATIENT)1[1].(ORDER)2[*].(ORC)1[1].(ORC-2)2[1]
        List<Element> ORC2List = Query.query(e, "6[1].1[1].2[*].1[1].2[1]").get();
        Iterator<Element> it = ORC2List.iterator();
        Set<java.util.List<String>> values = new HashSet<java.util.List<String>>();
        while (it.hasNext()) {
            Element next = it.next();

            List<Simple> ORC2_1List = Query.queryAsSimple(next, "1[1]").get();
            List<Simple> ORC2_2List = Query.queryAsSimple(next, "2[1]").get();
            List<Simple> ORC2_3List = Query.queryAsSimple(next, "3[1]").get();
            List<Simple> ORC2_4List = Query.queryAsSimple(next, "4[1]").get();

            String ORC2_1 = ORC2_1List.size() > 0 ? ORC2_1List.apply(0).value().raw()
                    : "";
            String ORC2_2 = ORC2_2List.size() > 0 ? ORC2_2List.apply(0).value().raw()
                    : "";
            String ORC2_3 = ORC2_3List.size() > 0 ? ORC2_3List.apply(0).value().raw()
                    : "";
            String ORC2_4 = ORC2_4List.size() > 0 ? ORC2_4List.apply(0).value().raw()
                    : "";

            java.util.List<String> ORC2 = Arrays.asList(ORC2_1, ORC2_2, ORC2_3,
                    ORC2_4);

            if (values.contains(ORC2)) {
                return false;
            }
            values.add(ORC2);
        }
        return true;
    }
}
