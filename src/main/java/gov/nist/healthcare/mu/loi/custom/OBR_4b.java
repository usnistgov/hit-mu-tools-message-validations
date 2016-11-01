package gov.nist.healthcare.mu.loi.custom;

import hl7.v2.instance.Element;
import hl7.v2.instance.Query;
import scala.collection.immutable.List;

public class OBR_4b {

    public boolean assertion(hl7.v2.instance.Element e) {
        // get OBR-4 list
        List<Element> OBR_4List = Query.query(e, "4[*]").get();
        LOINC ln = new LOINC();
        return ln.checkSecondTriplet(OBR_4List);
    }
}
