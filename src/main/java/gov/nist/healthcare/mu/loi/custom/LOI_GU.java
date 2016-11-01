package gov.nist.healthcare.mu.loi.custom;

import java.util.Arrays;
import java.util.List;

public class LOI_GU extends MSH_21 {

    /*

     */

    private final static String oid_1 = "2.16.840.1.113883.9.85";
    private final static String oid_2 = "2.16.840.1.113883.9.86";

    public boolean assertion(hl7.v2.instance.Element e) {
        return super.assertion(e);
    }

    @Override
    public List<String> getOids() {
        return Arrays.asList(oid_1, oid_2);
    }
}
