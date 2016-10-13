package gov.nist.healthcare.mu.edos.custom;

import hl7.v2.instance.Query;
import hl7.v2.instance.Simple;
import org.apache.commons.lang3.StringUtils;
import scala.collection.Iterator;
import scala.collection.immutable.List;

/* NIST-4: The first part of OM4-1 (Sequence Number - Test/Observation Master File) SHALL 
 * contain the same value as the sequence number of the associated OM1 segment. 
 * The second part of OM4-1 (Sequence Number - Test/Observation Master File) SHALL be 
 * valued sequentially. 
 */

public class OM4 {

    /**
     * @param e
     *        MF_TEST context
     * @return
     */
    public boolean assertion(hl7.v2.instance.Element e) {
        boolean result1 = false;
        boolean result2 = false;

        // get OM4-1 List
        List<Simple> OM4_1List = Query.queryAsSimple(e, "7[*].1[1]").get();

        // get OM1-1
        List<Simple> OM1_1List = Query.queryAsSimple(e, "2[1].1[1]").get();
        String OM1_1 = getValue(OM1_1List);

        Iterator<Simple> OM4_1it = OM4_1List.iterator();
        int i = 0;
        while (OM4_1it.hasNext()) {
            i++;
            String OM4_1 = OM4_1it.next().value().raw();
            String[] split = StringUtils.split(OM4_1, '.');
            if (split.length == 0) {
                // not sure if that could happen, but just in case !
                return false;
            } else {
                // compare first part with OM1-1
                result1 = OM1_1.equals(split[0]);
                if (split.length > 1) {
                    // compare second part
                    result2 = String.valueOf(i).equals(split[1]);
                } else {
                    // when there is only one OM4, there could be no second part
                    // and that is fine
                    result2 = (i == 1);
                }
            }
        }
        return result1 && result2;
    }

    private String getValue(List<Simple> simpleElementList) {
        if (simpleElementList.size() > 1) {
            throw new IllegalArgumentException("Invalid List size : "
                    + simpleElementList.size());
        }
        if (simpleElementList.size() == 0) {
            return "";
        }
        // only get first element
        return simpleElementList.apply(0).value().raw();
    }

}
