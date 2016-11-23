package gov.nist.healthcare.mu.lri.custom;

import hl7.v2.instance.Element;
import hl7.v2.instance.Query;
import hl7.v2.instance.Simple;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import scala.collection.Iterator;
import scala.collection.immutable.List;

public class LRI_28 {

    /*
     * LRI-28: The value of ORC-3 (Filler Order Number) SHALL NOT be valued
     * identical to another instance of ORC-3 (Filler Order Number) in the same
     * message excluding the Prior Result group(s).
     */

    /**
     * @param e
     *        message context
     * @return true if assertion is verified
     */
    public boolean assertion(hl7.v2.instance.Element e) {
        List<Element> ORC3List = Query.query(e, "3[1].2[*].1[1].3[1]").get();
        Iterator<Element> it = ORC3List.iterator();
        Set<java.util.List<String>> values = new HashSet<java.util.List<String>>();
        while (it.hasNext()) {
            Element next = it.next();
            List<Simple> ORC3_1List = Query.queryAsSimple(next, "1[1]").get();
            List<Simple> ORC3_2List = Query.queryAsSimple(next, "2[1]").get();
            List<Simple> ORC3_3List = Query.queryAsSimple(next, "3[1]").get();
            List<Simple> ORC3_4List = Query.queryAsSimple(next, "4[1]").get();

            String ORC3_1 = ORC3_1List.size() > 0 ? ORC3_1List.apply(0).value().raw()
                    : "";
            String ORC3_2 = ORC3_2List.size() > 0 ? ORC3_2List.apply(0).value().raw()
                    : "";
            String ORC3_3 = ORC3_3List.size() > 0 ? ORC3_3List.apply(0).value().raw()
                    : "";
            String ORC3_4 = ORC3_4List.size() > 0 ? ORC3_4List.apply(0).value().raw()
                    : "";

            java.util.List<String> ORC3 = Arrays.asList(ORC3_1, ORC3_2, ORC3_3,
                    ORC3_4);
            if (values.contains(ORC3)) {
                return false;
            }
            values.add(ORC3);
        }
        return true;
    }
}
