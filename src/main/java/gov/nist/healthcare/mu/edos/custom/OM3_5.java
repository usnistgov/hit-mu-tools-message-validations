package gov.nist.healthcare.mu.edos.custom;

import hl7.v2.instance.Element;
import hl7.v2.instance.Query;
import scala.collection.immutable.List;

public class OM3_5 {

    public boolean assertion(hl7.v2.instance.Element e) {
        // get OM3-5 list
        List<Element> OM3_5List = Query.query(e, "5[*]").get();
        SNOMED_CT sct = new SNOMED_CT();
        return sct.checkFirstTriplet(OM3_5List);
    }
}
