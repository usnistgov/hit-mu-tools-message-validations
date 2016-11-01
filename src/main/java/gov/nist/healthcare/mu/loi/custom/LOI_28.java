package gov.nist.healthcare.mu.loi.custom;

import java.util.Arrays;
import java.util.List;

public class LOI_28 extends MSH_21 {

    /*
     * LOI-28: An occurrence of MSH-21 (Message Profile Identifier) SHALL be
     * valued with '2.16.840.1.113883.9.94'.
     */

    private final static String oid = "2.16.840.1.113883.9.94";

    public boolean assertion(hl7.v2.instance.Element e) {
        return super.assertion(e);
    }

    @Override
    public List<String> getOids() {
        return Arrays.asList(oid);
    }
}
