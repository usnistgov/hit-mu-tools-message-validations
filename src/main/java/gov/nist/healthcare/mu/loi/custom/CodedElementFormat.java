package gov.nist.healthcare.mu.loi.custom;

import hl7.v2.instance.Element;
import hl7.v2.instance.Query;
import hl7.v2.instance.Simple;
import org.apache.log4j.Logger;
import scala.collection.Iterator;
import scala.collection.immutable.List;

public abstract class CodedElementFormat {

    private static Logger logger = Logger.getLogger(CodedElementFormat.class.getName());

    public boolean isValid(String code) {
        return true;
    }

    public abstract String getCodeSystemValue();

    public boolean checkFirstTriplet(List<Element> elementList) {
        if (elementList != null) {
            Iterator<Element> it = elementList.iterator();
            while (it.hasNext()) {
                Element element = it.next();
                // get 3rd component
                List<Simple> codeSystemList = Query.queryAsSimple(element,
                        "3[1]").get();
                String codeSystem = getValue(codeSystemList);
                if (getCodeSystemValue().equals(codeSystem)) {
                    // get 1st component
                    List<Simple> identifierList = Query.queryAsSimple(element,
                            "1[1]").get();
                    String identifier = getValue(identifierList);
                    return isValid(identifier);
                }
            }
        }
        return true;
    }

    public boolean checkSecondTriplet(List<Element> elementList) {
        if (elementList != null) {
            Iterator<Element> it = elementList.iterator();
            while (it.hasNext()) {
                Element element = it.next();
                // get 3rd component
                List<Simple> codeSystemList = Query.queryAsSimple(element,
                        "6[1]").get();
                String codeSystem = getValue(codeSystemList);
                if (getCodeSystemValue().equals(codeSystem)) {
                    // get 1st component
                    List<Simple> identifierList = Query.queryAsSimple(element,
                            "4[1]").get();
                    String identifier = getValue(identifierList);
                    return isValid(identifier);
                }
            }
        }
        logger.warn("elementList is null");
        return true;
    }

    private static String getValue(List<Simple> simpleElementList) {
        if (simpleElementList.size() > 1) {
            throw new IllegalArgumentException("Invalid List size : "
                    + simpleElementList.size());
        }
        if (simpleElementList.size() == 0) {
            return "";
        }
        // only get first element
        return simpleElementList.apply(0).value().raw();
    }

}
