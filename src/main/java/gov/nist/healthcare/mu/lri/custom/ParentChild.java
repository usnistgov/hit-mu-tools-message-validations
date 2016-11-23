package gov.nist.healthcare.mu.lri.custom;

import hl7.v2.instance.Complex;
import hl7.v2.instance.Element;
import hl7.v2.instance.Query;
import hl7.v2.instance.Simple;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import scala.collection.immutable.List;
import com.google.common.collect.Sets.SetView;

public abstract class ParentChild {

    private static Logger logger = Logger.getLogger(ParentChild.class.getName());

    private Map<Integer, Map<String, String>> OBRs;

    /**
     * @param e
     *        message context : PATIENT_RESULT
     * @return true if assertion is verified
     */
    public boolean assertion(hl7.v2.instance.Element e) {
        OBRs = new HashMap<Integer, Map<String, String>>();
        List<Element> ooList = Query.query(e, "2[*]").get();
        // parse OBRs
        parseOBR(ooList);

        // detect child OBRs : get all OBRs where OBR-11 = G or OBR-26 valued
        for (Entry<Integer, Map<String, String>> OBRChildEntry : OBRs.entrySet()) {
            Integer childIdx = OBRChildEntry.getKey();
            Map<String, String> OBRChild = OBRChildEntry.getValue();
            String OBR1 = OBRChild.get("OBR-1");
            String OBR11 = OBRChild.get("OBR-11");
            String OBR26_1 = OBRChild.get("OBR-26.1.1");
            if ("G".equals(OBR11) || OBR26_1 != null) {
                // this is a child OBR
                // 1. detect parent OBR(s)
                SetView<Integer> parentOBRs = detectParentOBRs(OBRChildEntry);
                if (parentOBRs.size() == 0) {
                    // no parent OBR for the child !
                    logger.debug(String.format(
                            "No parent OBR found for child OBR [%s]", OBR1));
                    return false;
                }
                // 2. for each potential parent OBR, detect parent OBX
                boolean parentOBX = false;
                for (Integer parentIdx : parentOBRs) {
                    // should not happen
                    if (childIdx == parentIdx) {
                        // same OBR
                        continue;
                    }
                    Element ooParent = ooList.apply(parentIdx);
                    List<Simple> parentOBR1List = Query.queryAsSimple(ooParent,
                            "2[1].1[1]").get();
                    Simple parentOBR1 = getSimple(parentOBR1List);
                    logger.debug(String.format(
                            "Detected parent OBR [%s] for child OBR [%s]",
                            parentOBR1.value().raw(), OBR1));

                    SetView<Integer> parentOBXs = detectParentOBXs(ooParent,
                            OBRChildEntry);
                    if (parentOBXs.size() > 0) {
                        // we found a matching parent OBX under the parent OBR
                        // don't return true here as there might be other child
                        // OBRs to check

                        // we can break the loop and go to next child OBR
                        logger.debug(String.format(
                                "  Parent OBX found for child OBR [%s] under parent OBR [%s]",
                                OBR1, parentOBR1.value().raw()));
                        parentOBX = true;
                        break;
                    } else {
                        // didn't find a matching OBX under this parent OBR. Go
                        // to next potential parent OBR
                        logger.debug(String.format(
                                "  No parent OBX found for child OBR [%s] under parent OBR [%s]",
                                OBR1, parentOBR1.value().raw()));
                        continue;
                    }
                }

                if (!parentOBX) {
                    // we went through all OBX after all potential parent OBR
                    // and did not find a match for this child OBR
                    logger.debug(String.format(
                            "Result : No parent OBX found for child OBR [%s] under any parent OBR",
                            OBR1));
                    return false;
                } else {
                    logger.debug(String.format(
                            "Result : Parent OBX found for child OBR [%s] under a parent OBR",
                            OBR1));
                }

            }
            // not a child OBR - do nothing
        }
        return true;
    }

    protected abstract SetView<Integer> detectParentOBRs(
            Entry<Integer, Map<String, String>> OBRChildEntry);

    protected abstract SetView<Integer> detectParentOBXs(Element OBRParent,
            Entry<Integer, Map<String, String>> OBRChildEntry);

    protected Set<Integer> checkRule29_1(int childIdx,
            Map<String, String> OBRChild29_1) {
        Set<Integer> rule = new HashSet<Integer>();
        for (Entry<Integer, Map<String, String>> OBRParentEntry : OBRs.entrySet()) {
            Integer parentIdx = OBRParentEntry.getKey();
            if (childIdx == parentIdx) {
                // same OBR
                continue;
            }

            Map<String, String> OBRParent = OBRParentEntry.getValue();
            // extract OBR-2
            Map<String, String> OBRParent2 = extract("OBR-2", OBRParent);
            if (OBRChild29_1.size() != OBRParent2.size()) {
                // does not match
                continue;
            } else if (OBRChild29_1.size() == 0) {
                rule.add(parentIdx);
            } else {
                // check if match
                boolean match = false;
                for (String childLocation : OBRChild29_1.keySet()) {
                    String childValue = OBRChild29_1.get(childLocation);
                    String parentLocation = StringUtils.replace(childLocation,
                            "OBR-29.1", "OBR-2", 1);
                    String parentValue = OBRParent2.get(parentLocation);

                    if (!childValue.equals(parentValue)) {
                        // no match
                        match = false;
                        break;
                    }
                    match = true;
                }
                if (match) {
                    // we found a match !
                    rule.add(parentIdx);
                }
            }
        }
        return rule;
    }

    protected Set<Integer> checkRule29_2(int childIdx,
            Map<String, String> OBRChild29_2) {
        Set<Integer> rule = new HashSet<Integer>();
        for (Entry<Integer, Map<String, String>> OBRParentEntry : OBRs.entrySet()) {
            Integer parentIdx = OBRParentEntry.getKey();
            if (childIdx == parentIdx) {
                // same OBR
                continue;
            }
            Map<String, String> OBRParent = OBRParentEntry.getValue();
            // extract OBR-3
            Map<String, String> OBRParent3 = extract("OBR-3", OBRParent);
            if (OBRChild29_2.size() != OBRParent3.size()) {
                // does not match
                continue;
            } else if (OBRChild29_2.size() == 0) {
                rule.add(parentIdx);
            } else {
                // check if match
                boolean match = false;
                for (String childLocation : OBRChild29_2.keySet()) {
                    String childValue = OBRChild29_2.get(childLocation);
                    String parentLocation = StringUtils.replace(childLocation,
                            "OBR-29.2", "OBR-3", 1);
                    String parentValue = OBRParent3.get(parentLocation);
                    if (!childValue.equals(parentValue)) {
                        // no match
                        match = false;
                        break;
                    }
                    match = true;
                }
                if (match) {
                    // we found a match !
                    rule.add(parentIdx);
                }
            }
        }
        return rule;
    }

    protected Set<Integer> checkRule26_1(Element OBRParent,
            Map<String, String> OBRChild26_1) {
        Set<Integer> rule = new HashSet<Integer>();
        // get the list of OBXs under this OBR
        List<Element> OBXs = Query.query(OBRParent, "6[*].1[1]").get();
        for (int parentOBXIdx = 0; parentOBXIdx < OBXs.size(); parentOBXIdx++) {
            Element OBX = OBXs.apply(parentOBXIdx);
            // OBX-3 (CWE)
            Map<String, String> OBXParent3 = new HashMap<String, String>();
            List<Element> OBX3List = Query.query(OBX, "3[1]").get();
            Element OBX3 = getElement(OBX3List);
            OBXParent3.putAll(pair(OBX3));

            if (OBRChild26_1.size() != OBXParent3.size()) {
                // does not match
                continue;
            } else if (OBRChild26_1.size() == 0) {
                rule.add(parentOBXIdx);
            } else {
                // check if match
                boolean match = false;
                for (String childLocation : OBRChild26_1.keySet()) {
                    String childValue = OBRChild26_1.get(childLocation);
                    String parentLocation = StringUtils.replace(childLocation,
                            "OBR-26.1", "OBX-3", 1);
                    String parentValue = OBXParent3.get(parentLocation);
                    if (!childValue.equals(parentValue)) {
                        // no match
                        match = false;
                        break;
                    }
                    match = true;
                }
                if (match) {
                    // we found a match !
                    rule.add(parentOBXIdx);
                }
            }
        }
        return rule;
    }

    protected Set<Integer> checkRule26_2(Element OBRParent,
            Map<String, String> OBRChild26_2) {
        Set<Integer> rule = new HashSet<Integer>();
        // get the list of OBXs under this OBR
        List<Element> OBXs = Query.query(OBRParent, "6[*].1[1]").get();
        for (int parentOBXIdx = 0; parentOBXIdx < OBXs.size(); parentOBXIdx++) {
            Element OBX = OBXs.apply(parentOBXIdx);
            // OBX-4 (OG)
            Map<String, String> OBXParent4 = new HashMap<String, String>();
            List<Element> OBX4List = Query.query(OBX, "4[1]").get();
            Element OBX4 = getElement(OBX4List);
            OBXParent4.putAll(pair(OBX4));
            // print(OBXParent4);

            if (OBRChild26_2.size() != OBXParent4.size()) {
                // does not match
                continue;
            } else if (OBRChild26_2.size() == 0) {
                rule.add(parentOBXIdx);
            } else {
                // check if match
                boolean match = false;
                for (String childLocation : OBRChild26_2.keySet()) {
                    String childValue = OBRChild26_2.get(childLocation);
                    String parentLocation = StringUtils.replace(childLocation,
                            "OBR-26.2", "OBX-4", 1);
                    String parentValue = OBXParent4.get(parentLocation);
                    if (!childValue.equals(parentValue)) {
                        // no match
                        match = false;
                        break;
                    }
                    match = true;
                }
                if (match) {
                    // we found a match !
                    rule.add(parentOBXIdx);
                }
            }
        }
        return rule;
    }

    protected Set<Integer> checkRule50(int childIdx,
            Map<String, String> OBRChild50) {
        Set<Integer> rule = new HashSet<Integer>();
        // print(OBRChild50);

        for (Entry<Integer, Map<String, String>> OBRParentEntry : OBRs.entrySet()) {
            Integer parentIdx = OBRParentEntry.getKey();
            if (childIdx == parentIdx) {
                // same OBR
                continue;
            }
            Map<String, String> OBRParent = OBRParentEntry.getValue();
            // extract OBR-4
            Map<String, String> OBRParent4 = extract("OBR-4", OBRParent);
            // print(OBRParent4);
            if (OBRChild50.size() != OBRParent4.size()) {
                // does not match
                continue;
            } else if (OBRChild50.size() == 0) {
                rule.add(parentIdx);
            } else {
                // check if match
                boolean match = false;
                for (String childLocation : OBRChild50.keySet()) {
                    String childValue = OBRChild50.get(childLocation);
                    String parentLocation = StringUtils.replace(childLocation,
                            "OBR-50", "OBR-4", 1);
                    String parentValue = OBRParent4.get(parentLocation);
                    if (!childValue.equals(parentValue)) {
                        // no match
                        match = false;
                        break;
                    }
                    match = true;
                }
                if (match) {
                    // we found a match !
                    rule.add(parentIdx);
                }
            }
        }
        return rule;
    }

    protected void parseOBR(List<Element> ooList) {
        // parse OBRs
        for (int i = 0; i < ooList.size(); i++) {
            Element ooGroup = ooList.apply(i);
            Element OBR = getOBR(ooGroup);
            Map<String, String> m = new HashMap<String, String>();
            // OBR-1 (EI)
            List<Simple> OBR1List = Query.queryAsSimple(OBR, "1[1]").get();
            Simple OBR1 = getSimple(OBR1List);
            m.putAll(pair(OBR1));

            // OBR-2 (EI)
            List<Element> OBR2List = Query.query(OBR, "2[1]").get();
            Element OBR2 = getElement(OBR2List);
            m.putAll(pair(OBR2));

            // OBR-3 (EI)
            List<Element> OBR3List = Query.query(OBR, "3[1]").get();
            Element OBR3 = getElement(OBR3List);
            m.putAll(pair(OBR3));

            // OBR-4 (CWE)
            List<Element> OBR4List = Query.query(OBR, "4[1]").get();
            Element OBR4 = getElement(OBR4List);
            m.putAll(pair(OBR4));

            // OBR-11 (ID)
            List<Simple> OBR11List = Query.queryAsSimple(OBR, "11[1]").get();
            Simple OBR11 = getSimple(OBR11List);
            m.putAll(pair(OBR11));

            // OBR-26 (PRL)
            List<Element> OBR26List = Query.query(OBR, "26[1]").get();
            Element OBR26 = getElement(OBR26List);
            m.putAll(pair(OBR26));

            // OBR-29 (EIP)
            List<Element> OBR29List = Query.query(OBR, "29[1]").get();
            Element OBR29 = getElement(OBR29List);
            m.putAll(pair(OBR29));

            // OBR-50 (CWE)
            List<Element> OBR50List = Query.query(OBR, "50[1]").get();
            Element OBR50 = getElement(OBR50List);
            m.putAll(pair(OBR50));

            OBRs.put(i, m);
        }

    }

    /* Utilities methods */

    protected Map<String, String> extract(String prefix, Map<String, String> map) {
        Map<String, String> result = new HashMap<String, String>();
        result.putAll(map);
        Set<String> c = new HashSet<String>();
        for (String key : map.keySet()) {
            if (key.startsWith(prefix)) {
                c.add(key);
            }
        }
        result.keySet().retainAll(c);
        return result;
    }

    protected Element getOBR(hl7.v2.instance.Element order_observation) {
        List<Element> OBRList = Query.query(order_observation, "2[1]").get();
        Element OBR = getElement(OBRList);
        return OBR;
    }

    protected Map<String, String> pair(Element e) {
        Map<String, String> map = new HashMap<String, String>();
        if (e != null && e instanceof Complex) {
            map.putAll(pair((Complex) e));
        }
        if (e != null && e instanceof Simple) {
            map.putAll(pair((Simple) e));
        }
        return map;
    }

    protected Map<String, String> pair(Complex c) {
        Map<String, String> map = new HashMap<String, String>();
        List<Element> children = c.children();
        for (int i = 0; i < children.size(); i++) {
            Element child = children.apply(i);
            if (child instanceof Simple) {
                Map<String, String> m = pair((Simple) child);
                map.putAll(m);
            }
            if (child instanceof Complex) {
                Map<String, String> m = pair((Complex) child);
                map.putAll(m);
            }
        }
        return map;
    }

    protected Map<String, String> pair(Simple s) {
        Map<String, String> map = new HashMap<String, String>();
        if (s != null) {
            map.put(s.location().path(), s.value().raw());
        }
        return map;
    }

    protected Simple getSimple(List<Simple> simpleElementList) {
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

    protected Element getElement(List<Element> elementList) {
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

    /* Utilities to print message values */

    protected String getComplexValue(Complex c, String prefix) {
        StringBuffer sb = new StringBuffer();
        String path = c.location().path();
        List<Element> children = c.children();
        sb.append(prefix);
        sb.append(path);
        sb.append(" = [\n");
        for (int i = 0; i < children.size(); i++) {
            if (i > 0) {
                sb.append("\n");
            }
            Element child = children.apply(i);
            if (child instanceof Complex) {
                sb.append(getComplexValue((Complex) child, prefix + "  ").toString());
            } else if (child instanceof Simple) {
                sb.append(getSimpleValue((Simple) child, prefix + "  ").toString());
            }
        }
        sb.append("\n");
        sb.append(prefix);
        sb.append("]");
        return sb.toString();
    }

    protected String getSimpleValue(Simple s, String prefix) {
        String path = s.location().path();
        return String.format("%s%s = %s", prefix, path, s.value().raw());
    }

    // private void print(Map<String, String> map) {
    // System.err.println();
    // for (Entry<String, String> entry : map.entrySet()) {
    // System.err.println(entry.getKey() + " " + entry.getValue());
    // }
    // System.err.println();
    // }
}
