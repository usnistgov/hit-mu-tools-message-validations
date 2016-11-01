package gov.nist.healthcare.mu.loi.custom;

import java.util.Arrays;
import java.util.List;

public class LOI_14 extends MSH_21 {

    /*
     * LOI-14: An occurrence of MSH-21 (Message Profile Identifier) SHALL be
     * valued with ‘2.16.840.1.113883.9.86’ (LOI_GU_PRN_Profile) or three
     * occurrences SHALL be valued with ‘2.16.840.1.113883.9.66’
     * (LOI_Common_Component), ‘2.16.840.1.113883.9.78’ (LOI_GU_Component) and
     * ‘2.16.840.1.113883.9.81’ (LAB_PRN_Component) in any order.
     */

    private final static String oid = "2.16.840.1.113883.9.86";

    public boolean assertion(hl7.v2.instance.Element e) {
        return super.assertion(e);
    }

    @Override
    public List<String> getOids() {
        return Arrays.asList(oid);
    }
}
