package gov.nist.healthcare.mu.loi.custom;

import java.util.Arrays;
import java.util.List;

public class LOI_26 extends MSH_21 {

    /*
     * LOI-26: MSH-21 (Message Profile Identifier) SHALL be valued with
     * ‘2.16.840.1.113883.9.90’ (LOI_GU_Acknowledgment_Component) when
     * acknowledging OML GU Profiles where MSH-21 contains
     * ‘2.16.840.1.113883.9.85’ (LOI_GU_PRU_Profile), or
     * ‘2.16.840.1.113883.9.86’ (LOI_GU_PRN_Profile), or
     * ‘2.16.840.1.113883.9.78’ (LOI_GU_Component).
     */

    private final static String oid = "2.16.840.1.113883.9.90";

    public boolean assertion(hl7.v2.instance.Element e) {
        return super.assertion(e);
    }

    @Override
    public List<String> getOids() {
        return Arrays.asList(oid);
    }
}
