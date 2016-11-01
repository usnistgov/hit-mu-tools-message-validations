package gov.nist.healthcare.mu.loi.custom;

import java.util.Arrays;
import java.util.List;

public class LOI_16 extends MSH_21 {

    /*
     * LOI-16: An occurrence of MSH-21 (Message Profile Identifier) SHALL be
     * valued with ‘2.16.840.1.113883.9.88’ (LOI_NG_PRN_Profile) or three
     * occurrences SHALL be valued with ‘2.16.840.1.113883.9.66’
     * (LOI_Common_Component), ‘2.16.840.1.113883.9.79’ (LOI_NG_Component) and
     * ‘2.16.840.1.113883.9.81’ (LAB_PRN_Component) in any order.
     */

    private final static String oid = "2.16.840.1.113883.9.88";

    public boolean assertion(hl7.v2.instance.Element e) {
        return super.assertion(e);
    }

    @Override
    public List<String> getOids() {
        return Arrays.asList(oid);
    }
}
