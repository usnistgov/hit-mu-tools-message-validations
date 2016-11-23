package gov.nist.healthcare.mu.lri.custom;

import hl7.v2.instance.Element;
import hl7.v2.instance.Query;
import hl7.v2.instance.Simple;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import scala.collection.Iterator;
import scala.collection.immutable.List;

public class LRI_40 {

    /*
     * LRI-40: The value of OBR-3 (Filler Order Number) SHALL NOT be valued
     * identical to another instance of OBR-3 (Filler Order Number) in the
     * message.
     */

    /**
     * @param e
     *        message context
     * @return true if assertion is verified
     */
    public boolean assertion(hl7.v2.instance.Element e) {
        List<Element> OBR3List = Query.query(e, "3[1].2[*].2[1].3[1]").get();
        Iterator<Element> it = OBR3List.iterator();
        Set<java.util.List<String>> values = new HashSet<java.util.List<String>>();
        while (it.hasNext()) {
            Element next = it.next();

            List<Simple> OBR3_1List = Query.queryAsSimple(next, "1[1]").get();
            List<Simple> OBR3_2List = Query.queryAsSimple(next, "2[1]").get();
            List<Simple> OBR3_3List = Query.queryAsSimple(next, "3[1]").get();
            List<Simple> OBR3_4List = Query.queryAsSimple(next, "4[1]").get();

            String OBR3_1 = OBR3_1List.size() > 0 ? OBR3_1List.apply(0).value().raw()
                    : "";
            String OBR3_2 = OBR3_2List.size() > 0 ? OBR3_2List.apply(0).value().raw()
                    : "";
            String OBR3_3 = OBR3_3List.size() > 0 ? OBR3_3List.apply(0).value().raw()
                    : "";
            String OBR3_4 = OBR3_4List.size() > 0 ? OBR3_4List.apply(0).value().raw()
                    : "";

            java.util.List<String> OBR3 = Arrays.asList(OBR3_1, OBR3_2, OBR3_3,
                    OBR3_4);

            if (values.contains(OBR3)) {
                return false;
            }
            values.add(OBR3);
        }
        return true;
    }
}
