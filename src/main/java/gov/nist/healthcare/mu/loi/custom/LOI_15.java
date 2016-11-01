package gov.nist.healthcare.mu.loi.custom;

import java.util.Arrays;
import java.util.List;

public class LOI_15 extends MSH_21 {

    /*
     * LOI-15: An occurrence of MSH-21 (Message Profile Identifier) SHALL be
     * valued with ‘2.16.840.1.113883.9.87’ (LOI_NG_PRU_Profile) or three
     * occurrences SHALL be valued with ‘2.16.840.1.113883.9.66’
     * (LOI_Common_Component), ‘2.16.840.1.113883.9.79’ (LOI_NG_Component) and
     * ‘2.16.840.1.113883.9.82’ (LAB_PRU_Component) in any order.
     */

    private final static String oid = "2.16.840.1.113883.9.87";

    public boolean assertion(hl7.v2.instance.Element e) {
        return super.assertion(e);
    }

    @Override
    public List<String> getOids() {
        return Arrays.asList(oid);
    }
}
