package gov.nist.healthcare.mu.lri.custom;

import hl7.v2.instance.Complex;
import hl7.v2.instance.Element;
import hl7.v2.instance.Query;
import hl7.v2.instance.Simple;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import scala.collection.immutable.List;

public class NIST_OBR50 {
    /*
     * If OBR-50 (Parent Universal Service Identifier) is valued, then there
     * SHALL be at least one other OBR in the message where OBR-4 (Universal
     * Service Identifier) = OBR-50 (Parent Universal Service Identifier)
     */

    public boolean assertion(hl7.v2.instance.Element e) {
        HashSet<String> OBR_50_paths = new HashSet<String>();
        HashSet<String> OBR_paths = new HashSet<String>();

        // ORDER
        List<Element> ORDER_List = Query.query(e, "2[*]").get();
        for (int i = 0; i < ORDER_List.size(); i++) {
            Element ORDER = ORDER_List.apply(i);
            String ORDER_path = String.format("%s[%s]", ORDER.position(),
                    ORDER.instance());

            // OBR-50
            List<Element> OBR_50_List = Query.query(ORDER, "2[1].50[1]").get();
            if (OBR_50_List != null && OBR_50_List.size() > 0) {
                Element OBR_50 = getElement(OBR_50_List);
                if (OBR_50 != null) {
                    String OBR_50_path = String.format("%s.2[1].50[1]",
                            ORDER_path);
                    OBR_50_paths.add(OBR_50_path);
                }
            }

            // OBRs
            List<Element> OBR_List = Query.query(ORDER, "2[1]").get();
            if (OBR_List != null && OBR_List.size() > 0) {
                Element OBR = getElement(OBR_List);
                if (OBR != null) {
                    String OBR_path = String.format("%s.2[1]", ORDER_path);
                    OBR_paths.add(OBR_path);
                }
            }
        }

        for (String OBR_50_path : OBR_50_paths) {

            // System.err.println(OBR_50_path);

            List<Element> OBR_50_List = Query.query(e, OBR_50_path).get();
            Element OBR_50 = getElement(OBR_50_List);
            Map<Integer, String> m1 = new HashMap<Integer, String>();
            m1.putAll(pair(OBR_50));

            // System.err.println(m1);

            boolean match = false;

            for (String OBR_path : OBR_paths) {
                // we don't want to test against the same OBR
                if (OBR_50_path.startsWith(OBR_path)) {
                    continue;
                }
                // System.err.println(OBR_path);

                List<Element> OBR_4_List = Query.query(e,
                        String.format("%s.4[1]", OBR_path)).get();
                Element OBR_4 = getElement(OBR_4_List);
                Map<Integer, String> m3 = new HashMap<Integer, String>();
                m3.putAll(pair(OBR_4));

                // System.err.println(m3);

                if (m1.equals(m3)) {
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
