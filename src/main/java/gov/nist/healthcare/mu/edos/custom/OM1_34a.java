package gov.nist.healthcare.mu.edos.custom;

import hl7.v2.instance.Element;
import hl7.v2.instance.Query;
import scala.collection.immutable.List;

public class OM1_34a {

    public boolean assertion(hl7.v2.instance.Element e) {
        // get OM1-34 list
        List<Element> OM1_34List = Query.query(e, "34[*]").get();
        LOINC ln = new LOINC();
        return ln.checkFirstTriplet(OM1_34List);
    }
}
