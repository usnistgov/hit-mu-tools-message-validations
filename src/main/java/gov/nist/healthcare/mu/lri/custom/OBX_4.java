package gov.nist.healthcare.mu.lri.custom;

import hl7.v2.instance.Simple;
import scala.collection.immutable.List;

public class OBX_4 {

    /*
     * Condition Predicate: If there are multiple OBX segments associated with
     * the same OBR segment that have the same OBX-3 values for (OBX-3.1 and
     * OBX-3.3) or (OBX-3.4 and OBX-3.6).
     */

    /**
     * @param e
     *        ORDER_OBSERVATION context
     * @return true if assertion is verified
     */

    public boolean assertion(hl7.v2.instance.Element e) {

        /**
         * TODO not sure this one is implemetable as a predicate because of the
         * repeteable elements, the context and the access of data in other
         * segments.
         */

        // List<Element> OBXList = Query.query(e, "6[*].1[1]").get();
        // HashSet<java.util.List<String>> values = new
        // HashSet<java.util.List<String>>();
        // // only test if there are multiple OBX (more than one)
        // if (OBXList.size() > 1) {
        // Iterator<Element> it = OBXList.iterator();
        // while (it.hasNext()) {
        // Element next = it.next();
        // // OBX-3.1
        // List<Simple> OBX3_1List = Query.queryAsSimple(next,
        // "3[1].1[1]").get();
        // String OBX3_1 = getValue(OBX3_1List);
        //
        // // OBX-3.3
        // List<Simple> OBX3_3List = Query.queryAsSimple(next,
        // "3[1].3[1]").get();
        // String OBX3_3 = getValue(OBX3_3List);
        //
        // // OBX-3.4 & OBX-3.6 can be empty
        // // OBX-3.4
        // List<Simple> OBX3_4List = Query.queryAsSimple(next,
        // "3[1].4[1]").get();
        // String OBX3_4 = getValue(OBX3_4List);
        //
        // // OBX-3.6
        // List<Simple> OBX3_6List = Query.queryAsSimple(next,
        // "3[1].6[1]").get();
        // String OBX3_6 = getValue(OBX3_6List);
        //
        // java.util.List<String> combi1 = Arrays.asList(OBX3_1, OBX3_3);
        // java.util.List<String> combi2 = Arrays.asList(OBX3_4, OBX3_6);
        //
        // if (values.contains(combi1) || values.contains(combi2)) {
        // return true;
        // }
        // // do not add empty
        // if (!combi1.equals(Arrays.asList("", ""))) {
        // values.add(combi1);
        // }
        // if (!combi2.equals(Arrays.asList("", ""))) {
        // values.add(combi2);
        // }
        // }
        // }
        return false;
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
