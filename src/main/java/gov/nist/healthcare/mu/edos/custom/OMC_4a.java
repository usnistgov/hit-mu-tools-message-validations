package gov.nist.healthcare.mu.edos.custom;

import hl7.v2.instance.Element;
import hl7.v2.instance.Query;
import scala.collection.immutable.List;

public class OMC_4a {

    public boolean assertion(hl7.v2.instance.Element e) {
        // get OMC-4list
        List<Element> OMC_4List = Query.query(e, "4[*]").get();
        LOINC ln = new LOINC();
        return ln.checkFirstTriplet(OMC_4List);
    }
}
