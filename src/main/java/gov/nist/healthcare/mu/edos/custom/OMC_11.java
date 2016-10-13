package gov.nist.healthcare.mu.edos.custom;

import hl7.v2.instance.Element;
import hl7.v2.instance.Query;
import scala.collection.immutable.List;

public class OMC_11 {

    public boolean assertion(hl7.v2.instance.Element e) {
        // get OMC-11list
        List<Element> OMC_11List = Query.query(e, "11[*]").get();
        SNOMED_CT sct = new SNOMED_CT();
        return sct.checkFirstTriplet(OMC_11List);
    }
}
