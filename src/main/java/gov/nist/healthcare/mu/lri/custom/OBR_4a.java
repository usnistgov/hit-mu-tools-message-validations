package gov.nist.healthcare.mu.lri.custom;

import hl7.v2.instance.Element;
import hl7.v2.instance.Query;
import scala.collection.immutable.List;

public class OBR_4a {

    public boolean assertion(hl7.v2.instance.Element e) {
        // get OBR-4 list
        List<Element> OBR_4List = Query.query(e, "4[*]").get();
        LOINC ln = new LOINC();
        return ln.checkFirstTriplet(OBR_4List);
    }
}
