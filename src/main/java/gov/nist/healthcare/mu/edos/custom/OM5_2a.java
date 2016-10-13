package gov.nist.healthcare.mu.edos.custom;

import hl7.v2.instance.Element;
import hl7.v2.instance.Query;
import scala.collection.immutable.List;

public class OM5_2a {

    public boolean assertion(hl7.v2.instance.Element e) {
        // get OM5-2 list
        List<Element> OM5_2List = Query.query(e, "2[*]").get();
        LOINC ln = new LOINC();
        return ln.checkFirstTriplet(OM5_2List);
    }
}
