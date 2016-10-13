package gov.nist.healthcare.mu.edos.custom;

import hl7.v2.instance.Element;
import hl7.v2.instance.Query;
import scala.collection.immutable.List;

public class OM3_4 {

    public boolean assertion(hl7.v2.instance.Element e) {
        // get OM3-4 list
        List<Element> OM3_4List = Query.query(e, "4[*]").get();
        SNOMED_CT sct = new SNOMED_CT();
        return sct.checkFirstTriplet(OM3_4List);
    }
}
