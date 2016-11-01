package gov.nist.healthcare.mu.loi.custom;

import hl7.v2.instance.Element;
import hl7.v2.instance.Query;
import scala.collection.immutable.List;

public class OBX_3a {

    public boolean assertion(hl7.v2.instance.Element e) {
        // get OBX-3 list
        List<Element> OBX_3List = Query.query(e, "3[*]").get();
        LOINC ln = new LOINC();
        return ln.checkFirstTriplet(OBX_3List);
    }
}
