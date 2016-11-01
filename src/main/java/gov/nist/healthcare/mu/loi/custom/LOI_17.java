package gov.nist.healthcare.mu.loi.custom;

import java.util.Arrays;
import java.util.List;

public class LOI_17 extends MSH_21 {

    /*
     * LOI-17: An occurrence of MSH-21 (Message Profile Identifier) SHALL be
     * valued with ‘2.16.840.1.113883.9.85’ (LOI_GU_PRU_Profile) or three
     * occurrences SHALL be valued with ‘2.16.840.1.113883.9.66’
     * (LOI_Common_Component), ‘2.16.840.1.113883.9.78’ (LOI_GU_Component) and
     * ‘2.16.840.1.113883.9.82’ (LAB_PRU_Component) in any order.
     */

    private final static String oid = "2.16.840.1.113883.9.85";

    public boolean assertion(hl7.v2.instance.Element e) {
        return super.assertion(e);
    }

    @Override
    public List<String> getOids() {
        return Arrays.asList(oid);
    }
}
