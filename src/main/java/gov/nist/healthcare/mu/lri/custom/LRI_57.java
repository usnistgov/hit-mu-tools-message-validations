package gov.nist.healthcare.mu.lri.custom;

import hl7.v2.instance.Element;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.log4j.Logger;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

public class LRI_57 extends ParentChild {

    private static Logger logger = Logger.getLogger(LRI_57.class.getName());

    /*
     * LRI-57: Results with a Parent/Child relationship (as defined in section
     * 6.1.1), such as Microbiology and Reflex Results must provide proper
     * linking from the Child result to the Parent OBR and OBX as detailed
     * below:
     */
    /*
     * Parent OBR matching: Any OBR with a value of ‘G’ in OBR-11 (Specimen
     * Action Code) or a value in OBR-26 (Parent Result), henceforth referred to
     * as the Child OBR, SHALL be successfully matched to a Parent OBR in a
     * previously occurring Order Observation Group in the following ways:
     */
    /*
     * 1. Child OBR-29.1 (Placer Assigned Identifier) is valued the same as the
     * Parent OBR-2 (Placer Order Number) value (taking into account the
     * conversion of component delimiters into sub-component delimiters.)
     */
    /*
     * 2. AND The child OBR-29.2 (Filler Assigned Identifier) is valued the same
     * as the Parent OBR-3 (Filler Order Number) value (taking into account the
     * conversion of component delimiters into sub-component delimiters.)
     */
    /*
     * 3. AND The child OBR-50 (Parent Universal Service Identifier) is valued
     * the same as the Parent OBR-4 value.
     */
    /*
     * Parent OBX matching: Any OBR with a value of “G” in OBR-11 or a value in
     * OBR-26, henceforth referred to as the Child OBR, SHALL be successfully
     * matched to an OBX segment within the previously identified Parent Order
     * Observation Group in the following ways:
     */
    /*
     * 4. The child OBR-26.1 (Parent Observation Identifier) is valued the same
     * as the Parent OBX-3 value (taking into account the conversion of
     * component delimiters into sub-component delimiters.)
     */
    /*
     * 5. AND The child OBR-26.2 (Parent Observation Sub-Identifier) is valued
     * the same as the Parent OBX-4 (Observation Sub-Identifier) value (taking
     * into account the conversion of component delimiters into sub-component
     * delimiters.)
     */
    public boolean assertion(hl7.v2.instance.Element e) {
        return super.assertion(e);
    }

    protected SetView<Integer> detectParentOBRs(
            Entry<Integer, Map<String, String>> OBRChildEntry) {
        Set<Integer> rule1 = new HashSet<Integer>();
        Set<Integer> rule2 = new HashSet<Integer>();
        Set<Integer> rule3 = new HashSet<Integer>();
        Integer childIdx = OBRChildEntry.getKey();
        Map<String, String> OBRChild = OBRChildEntry.getValue();
        // extract OBR-29.1
        Map<String, String> OBRChild29_1 = extract("OBR-29.1", OBRChild);
        // extract OBR-29.2
        Map<String, String> OBRChild29_2 = extract("OBR-29.2", OBRChild);
        // extract OBR-50
        Map<String, String> OBRChild50 = extract("OBR-50", OBRChild);

        // detect parent OBR
        rule1 = checkRule29_1(childIdx, OBRChild29_1);
        rule2 = checkRule29_2(childIdx, OBRChild29_2);
        rule3 = checkRule50(childIdx, OBRChild50);

        // logger.debug(rule1);
        // logger.debug(rule2);
        // logger.debug(rule3);

        // rule 1 & rule 2 intersection
        Set<Integer> rule1AndRule2 = new HashSet<Integer>();
        Sets.intersection(rule1, rule2).copyInto(rule1AndRule2);
        // rule 1 and rule 2 and rule 3 intersection
        SetView<Integer> parentOBRs = Sets.intersection(rule1AndRule2, rule3);
        if (parentOBRs.size() == 0) {
            // no parent OBR for the child !
            // TODO error message for debug
        }
        return parentOBRs;
    }

    protected SetView<Integer> detectParentOBXs(Element OBRParent,
            Entry<Integer, Map<String, String>> OBRChildEntry) {
        Set<Integer> rule4 = new HashSet<Integer>();
        Set<Integer> rule5 = new HashSet<Integer>();
        Map<String, String> OBRChild = OBRChildEntry.getValue();
        // extract OBR-26.1
        Map<String, String> OBRChild26_1 = extract("OBR-26.1", OBRChild);
        // extract OBR-26.2
        Map<String, String> OBRChild26_2 = extract("OBR-26.2", OBRChild);
        rule4 = checkRule26_1(OBRParent, OBRChild26_1);
        rule5 = checkRule26_2(OBRParent, OBRChild26_2);
        SetView<Integer> parentOBXs = Sets.intersection(rule4, rule5);
        if (parentOBXs.size() == 0) {
            // no parent OBX for the child under the given parent OBR
            // TODO error message for debug
        }
        return parentOBXs;
    }

}
