package gov.nist.healthcare.mu.lri.custom;

import hl7.v2.instance.Element;
import hl7.v2.instance.Query;
import scala.collection.immutable.List;

public class OBX_5a {

    public boolean assertion(hl7.v2.instance.Element e) {
        // get OBX-5 list
        List<Element> OBX_5List = Query.query(e, "5[*]").get();
        SNOMED_CT sct = new SNOMED_CT();
        return sct.checkFirstTriplet(OBX_5List);
    }
}
