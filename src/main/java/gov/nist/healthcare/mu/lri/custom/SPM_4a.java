package gov.nist.healthcare.mu.lri.custom;

import hl7.v2.instance.Element;
import hl7.v2.instance.Query;
import scala.collection.immutable.List;

public class SPM_4a {

    public boolean assertion(hl7.v2.instance.Element e) {
        // get SMP-4 list
        List<Element> SPM_4List = Query.query(e, "4[*]").get();
        SNOMED_CT sct = new SNOMED_CT();
        return sct.checkFirstTriplet(SPM_4List);
    }
}
