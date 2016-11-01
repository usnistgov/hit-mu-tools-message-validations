package gov.nist.healthcare.mu.loi.custom;

import java.util.Arrays;
import java.util.List;

public class LOI_27 extends MSH_21 {

    /*
     * LOI-27: MSH-21 (Message Profile Identifier) SHALL be valued with
     * ‘2.16.840.1.113883.9.91’ (LOI_NG_Acknowledgment_Profile) when
     * acknowledging OML NG Profiles where MSH-21 contains
     * ‘2.16.840.1.113883.9.87’ (LOI_NG_PRU_Profile), or
     * ‘2.16.840.1.113883.9.88’ (LOI_NG_PRN_Profile), or
     * ‘2.16.840.1.113883.9.79’ (LOI_NG_Component).
     */

    private final static String oid = "2.16.840.1.113883.9.91";

    public boolean assertion(hl7.v2.instance.Element e) {
        return super.assertion(e);
    }

    @Override
    public List<String> getOids() {
        return Arrays.asList(oid);
    }
}
