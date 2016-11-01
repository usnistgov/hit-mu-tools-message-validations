package gov.nist.healthcare.mu.loi.custom;

import hl7.v2.instance.Query;
import hl7.v2.instance.Simple;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import scala.collection.Iterator;
import scala.collection.immutable.List;

public abstract class MSH_21 {

    private final static Map<String, java.util.List<String>> preCoordinated;

    static {
        preCoordinated = new HashMap<String, java.util.List<String>>();
        // LOI_GU_PRN_Profile
        preCoordinated.put("2.16.840.1.113883.9.86", Arrays.asList(
                "2.16.840.1.113883.9.66", "2.16.840.1.113883.9.78",
                "2.16.840.1.113883.9.81"));
        // LOI_NG_PRU_Profile
        preCoordinated.put("2.16.840.1.113883.9.87", Arrays.asList(
                "2.16.840.1.113883.9.66", "2.16.840.1.113883.9.79",
                "2.16.840.1.113883.9.82"));
        // LOI_NG_PRN_Profile
        preCoordinated.put("2.16.840.1.113883.9.88", Arrays.asList(
                "2.16.840.1.113883.9.66", "2.16.840.1.113883.9.79",
                "2.16.840.1.113883.9.81"));
        // LOI_GU_PRU_Profile
        preCoordinated.put("2.16.840.1.113883.9.85", Arrays.asList(
                "2.16.840.1.113883.9.66", "2.16.840.1.113883.9.78",
                "2.16.840.1.113883.9.82"));

    }

    public boolean assertion(hl7.v2.instance.Element e) {
        List<Simple> MSH21_3List = Query.queryAsSimple(e, "1[1].21[*].3[1]").get();
        Iterator<Simple> it = MSH21_3List.iterator();
        Set<String> values = new HashSet<String>();
        while (it.hasNext()) {
            Simple next = it.next();
            String value = next.value().raw();
            values.add(value);
        }
        return check(values);
    }

    private boolean check(Set<String> values) {
        if (getOids() == null || getOids().size() == 0) {
            return false;
        }
        for (String oid : getOids()) {
            if (values.contains(oid)) {
                // the oid is in the list from the message
                return true;
            }
            if (preCoordinated.keySet().contains(oid)) {
                // this is a pre-coordinated oid, let's check its components
                java.util.List<String> components = preCoordinated.get(oid);
                if (values.containsAll(components)) {
                    return true;
                }
            }
        }
        return false;
    }

    public abstract java.util.List<String> getOids();

}
