package gov.nist.healthcare.mu.loi.custom;

import hl7.v2.instance.Element;
import hl7.v2.instance.Query;
import scala.collection.immutable.List;

public class SPM_5a {

    public boolean assertion(hl7.v2.instance.Element e) {
        // get SMP-5 list
        List<Element> SPM_5List = Query.query(e, "5[*]").get();
        SNOMED_CT sct = new SNOMED_CT();
        return sct.checkFirstTriplet(SPM_5List);
    }
}
