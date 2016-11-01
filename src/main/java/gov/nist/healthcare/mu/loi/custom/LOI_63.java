package gov.nist.healthcare.mu.loi.custom;

import hl7.v2.instance.Element;
import hl7.v2.instance.Query;
import hl7.v2.instance.Simple;
import java.util.Arrays;
import java.util.HashSet;
import scala.collection.Iterator;
import scala.collection.immutable.List;

public class LOI_63 {

    /*
     * LOI-63: If there are multiple OBX segments associated with the same OBR
     * or SPM segment that have the same OBX-3 (Observation Identifier) values
     * for (OBX-3.1 (Identifier) and OBX-3.3 (Name of Coding System) or (OBX-3.4
     * (Alternate Identifier) and OBX-3.6 (Name of Alternate Coding System)), a
     * combination of (OBX-3.1 and OBX3.3) or (OBX-3.4 and OBX-3.6) and OBX-4
     * (Observation Sub-ID) SHALL create a unique identification under a single
     * OBR.
     */

    /**
     * @param e
     *        OBSERVATION_REQUEST context
     * @return
     */
    public boolean assertion(hl7.v2.instance.Element e) {
        List<Element> OBXList = Query.query(e, "7[*].1[1]").get();
        HashSet<java.util.List<String>> values = new HashSet<java.util.List<String>>();
        if (OBXList.size() > 1) {
            Iterator<Element> it = OBXList.iterator();
            while (it.hasNext()) {
                Element next = it.next();
                // OBX-3.1
                List<Simple> OBX3_1List = Query.queryAsSimple(next, "3[1].1[1]").get();
                String OBX3_1 = getValue(OBX3_1List);

                // OBX-3.3
                List<Simple> OBX3_3List = Query.queryAsSimple(next, "3[1].3[1]").get();
                String OBX3_3 = getValue(OBX3_3List);

                // OBX-3.4
                List<Simple> OBX3_4List = Query.queryAsSimple(next, "3[1].4[1]").get();
                String OBX3_4 = getValue(OBX3_4List);

                // OBX-3.6
                List<Simple> OBX3_6List = Query.queryAsSimple(next, "3[1].6[1]").get();
                String OBX3_6 = getValue(OBX3_6List);

                // OBX-4 is now a complex datatype. The whole OBX-4 make it
                // unique
                // OBX-4.1
                List<Simple> OBX4_1List = Query.queryAsSimple(next, "4[1].1[1]").get();
                String OBX4_1 = getValue(OBX4_1List);
                // OBX-4.2
                List<Simple> OBX4_2List = Query.queryAsSimple(next, "4[1].2[1]").get();
                String OBX4_2 = getValue(OBX4_2List);
                // OBX-4.3
                List<Simple> OBX4_3List = Query.queryAsSimple(next, "4[1].3[1]").get();
                String OBX4_3 = getValue(OBX4_3List);
                // OBX-4.4
                List<Simple> OBX4_4List = Query.queryAsSimple(next, "4[1].4[1]").get();
                String OBX4_4 = getValue(OBX4_4List);

                java.util.List<String> combi1 = Arrays.asList(OBX3_1, OBX3_3,
                        OBX4_1, OBX4_2, OBX4_3, OBX4_4);
                java.util.List<String> combi2 = Arrays.asList(OBX3_4, OBX3_6,
                        OBX4_1, OBX4_2, OBX4_3, OBX4_4);
                if (values.contains(combi1) || values.contains(combi2)) {
                    // System.err.println("LOI-63");
                    // System.err.println(combi1);
                    // System.err.println(combi2);
                    // System.err.println(values);
                    return false;
                }
                // do not match on empty
                if (!combi1.equals(Arrays.asList("", "", "", "", "", ""))
                        && !combi1.equals(Arrays.asList("", "", OBX4_1, OBX4_2,
                                OBX4_3, OBX4_4))) {
                    values.add(combi1);
                }
                if (!combi2.equals(Arrays.asList("", "", "", "", "", ""))
                        && !combi2.equals(Arrays.asList("", "", OBX4_1, OBX4_2,
                                OBX4_3, OBX4_4))) {
                    values.add(combi2);
                }
            }
        }
        return true;
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
