package gov.nist.healthcare.mu.loi.custom;

import java.util.Arrays;
import java.util.List;

public class LOI_29 extends MSH_21 {

    /*
     * LOI-29: An occurrence of MSH-21 (Message Profile Identifier) SHALL be
     * valued with '2.16.840.1.113883.9.80'.
     */

    private final static String oid = "2.16.840.1.113883.9.80";

    public boolean assertion(hl7.v2.instance.Element e) {
        return super.assertion(e);
    }

    @Override
    public List<String> getOids() {
        return Arrays.asList(oid);
    }
}
