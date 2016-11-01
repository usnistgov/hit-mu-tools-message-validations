package gov.nist.healthcare.mu.loi.custom;

import hl7.v2.instance.Complex;
import hl7.v2.instance.Element;
import hl7.v2.instance.Query;
import hl7.v2.instance.Simple;
import scala.collection.Iterator;
import scala.collection.immutable.List;

public class LOI_58 {

    /*
     * For each PRT (Participant Information) where PRT-4.1
     * (Participation.Identifier) is valued ‘RCT’ there must be a corresponding
     * value in OBR-28 (Result Copies To) equal to PRT-5 (Participation Person).
     */

    /**
     * @param e
     *        OBSERVATION_REQUEST context
     * @return
     */
    public boolean assertion(hl7.v2.instance.Element e) {
        // this conformance statement only applies to "new and append order"
        List<Element> OBR28List = Query.query(e, "1[1].28[*]").get();
        java.util.List<String> OBR28Values = new java.util.ArrayList<String>();
        if (OBR28List.size() > 0) {
            Iterator<Element> it = OBR28List.iterator();
            while (it.hasNext()) {
                Element next = it.next();
                OBR28Values.add(asString(next));
            }
        }
        List<Element> PRTList = Query.query(e, "4[*]").get();
        if (PRTList.size() > 0) {
            Iterator<Element> it = PRTList.iterator();
            while (it.hasNext()) {
                Element next = it.next();
                List<Simple> PRT4_1List = Query.queryAsSimple(next, "4[1].1[1]").get();
                String PRT4_1 = PRT4_1List.apply(0).value().raw();
                if ("RCT".equals(PRT4_1)) {
                    // get PRT-5 value
                    List<Element> PRT5List = Query.query(next, "5[1]").get();
                    Element PRT5 = PRT5List.apply(0);
                    boolean b = OBR28Values.contains(asString(PRT5));
                    if (!b) {
                        // there is no corresponding value in the OBR-28 List
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private String asString(Element e) {
        StringBuilder sb = new StringBuilder();
        // Get the value of the simple element
        if (e instanceof Simple)
            sb.append(" # Value = '").append(((Simple) e).value().raw()).append(
                    "'");
        // Get the children of the complex element
        else {
            scala.collection.Iterator<Element> it = ((Complex) e).children().iterator();
            while (it.hasNext()) {
                sb.append("\n\t").append(asString(it.next()));
            }
        }
        return sb.toString();
    }
}
