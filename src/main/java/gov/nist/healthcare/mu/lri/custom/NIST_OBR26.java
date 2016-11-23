package gov.nist.healthcare.mu.lri.custom;

import hl7.v2.instance.Complex;
import hl7.v2.instance.Element;
import hl7.v2.instance.Query;
import hl7.v2.instance.Simple;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import scala.collection.immutable.List;

public class NIST_OBR26 {
    /*
     * If OBR-26 (Parent Result) is valued, then there SHALL be at least one OBX
     * in the message where OBX-3 (Observation Identifier) = OBR-26.1 (Parent
     * Observation Identifier) AND OBX-4 (Observation Sub-ID) = OBR-26.2 (Parent
     * Observation Sub-identifier)
     */

    public boolean assertion(hl7.v2.instance.Element e) {
        HashSet<String> OBR_paths = new HashSet<String>();
        HashSet<String> OBX_paths = new HashSet<String>();

        // ORDER
        List<Element> ORDER_List = Query.query(e, "2[*]").get();
        for (int i = 0; i < ORDER_List.size(); i++) {
            Element ORDER = ORDER_List.apply(i);
            String ORDER_path = String.format("%s[%s]", ORDER.position(),
                    ORDER.instance());

            // OBRs
            List<Element> OBR_26_List = Query.query(ORDER, "2[1].26[1]").get();
            if (OBR_26_List != null && OBR_26_List.size() > 0) {
                Element OBR_26 = getElement(OBR_26_List);
                if (OBR_26 != null) {
                    String OBR_path = String.format("%s.2[1].26[1]", ORDER_path);
                    OBR_paths.add(OBR_path);
                }
            }

            // OBXs
            List<Element> OBSERVATION_List = Query.query(ORDER, "6[*]").get();
            for (int j = 0; j < OBSERVATION_List.size(); j++) {
                Element OBSERVATION = OBSERVATION_List.apply(j);
                String OBSERVATION_path = String.format("%s.%s[%s]",
                        ORDER_path, OBSERVATION.position(),
                        OBSERVATION.instance());
                String OBX_path = String.format("%s.1[1]", OBSERVATION_path);
                OBX_paths.add(OBX_path);
            }
        }

        for (String OBR_path : OBR_paths) {

            List<Element> OBR_26_1_List = Query.query(e,
                    String.format("%s.1[1]", OBR_path)).get();
            Element OBR_26_1 = getElement(OBR_26_1_List);
            Map<Integer, String> m1 = new HashMap<Integer, String>();
            m1.putAll(pair(OBR_26_1));

            List<Element> OBR_26_2_List = Query.query(e,
                    String.format("%s.2[1]", OBR_path)).get();
            Element OBR_26_2 = getElement(OBR_26_2_List);
            Map<Integer, String> m2 = new HashMap<Integer, String>();
            m2.putAll(pair(OBR_26_2));

            // System.err.println(m1);
            // System.err.println(m2);

            boolean match = false;

            for (String OBX_path : OBX_paths) {
                List<Element> OBX_3_List = Query.query(e,
                        String.format("%s.3[1]", OBX_path)).get();
                Element OBX_3 = getElement(OBX_3_List);
                Map<Integer, String> m3 = new HashMap<Integer, String>();
                m3.putAll(pair(OBX_3));

                List<Element> OBX_4_List = Query.query(e,
                        String.format("%s.4[1]", OBX_path)).get();
                Element OBX_4 = getElement(OBX_4_List);
                Map<Integer, String> m4 = new HashMap<Integer, String>();
                m4.putAll(pair(OBX_4));

                // System.err.println(m3);
                // System.err.println(m4);

                if (m1.equals(m3) && m2.equals(m4)) {
                    // found a match - check next OBR
                    match = true;
                    break;
                }
            }
            if (!match) {
                return false;
            }
        }
        return true;
    }

    protected Map<Integer, String> pair(Element e) {
        Map<Integer, String> map = new HashMap<Integer, String>();
        if (e != null && e instanceof Complex) {
            map.putAll(pair((Complex) e));
        }
        if (e != null && e instanceof Simple) {
            map.putAll(pair((Simple) e));
        }
        return map;
    }

    protected Map<Integer, String> pair(Complex c) {
        Map<Integer, String> map = new HashMap<Integer, String>();
        List<Element> children = c.children();
        for (int i = 0; i < children.size(); i++) {
            Element child = children.apply(i);
            if (child instanceof Simple) {
                Map<Integer, String> m = pair((Simple) child);
                map.putAll(m);
            }
            if (child instanceof Complex) {
                Map<Integer, String> m = pair((Complex) child);
                map.putAll(m);
            }
        }
        return map;
    }

    protected Map<Integer, String> pair(Simple s) {
        Map<Integer, String> map = new HashMap<Integer, String>();
        if (s != null) {
            map.put(s.position(), s.value().raw());
        }
        return map;
    }

    private Simple getSimple(List<Simple> simpleElementList) {
        if (simpleElementList.size() > 1) {
            throw new IllegalArgumentException("Invalid List size : "
                    + simpleElementList.size());
        }
        if (simpleElementList.size() == 0) {
            return null;
        }
        // only get first element
        return simpleElementList.apply(0);
    }

    private Element getElement(List<Element> elementList) {
        if (elementList.size() > 1) {
            throw new IllegalArgumentException("Invalid List size : "
                    + elementList.size());
        }
        if (elementList.size() == 0) {
            return null;
        }
        // only get first element
        return elementList.apply(0);
    }
}
