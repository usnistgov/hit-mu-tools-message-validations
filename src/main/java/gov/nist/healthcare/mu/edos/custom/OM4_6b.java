package gov.nist.healthcare.mu.edos.custom;

import hl7.v2.instance.Element;
import hl7.v2.instance.Query;
import scala.collection.immutable.List;

public class OM4_6b {

    public boolean assertion(hl7.v2.instance.Element e) {
        // get OM4-6 list
        List<Element> OM4_6List = Query.query(e, "6[*]").get();
        SNOMED_CT sct = new SNOMED_CT();
        return sct.checkSecondTriplet(OM4_6List);
    }
}
