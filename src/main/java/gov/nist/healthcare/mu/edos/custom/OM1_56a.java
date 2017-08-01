package gov.nist.healthcare.mu.edos.custom;

import hl7.v2.instance.Element;
import hl7.v2.instance.Query;
import scala.collection.immutable.List;

public class OM1_56a {

    public boolean assertion(hl7.v2.instance.Element e) {
        // get OM1-56 list
        List<Element> OM1_56List = Query.query(e, "56[*]").get();
        LOINC ln = new LOINC();
        return ln.checkFirstTriplet(OM1_56List);
    }
}