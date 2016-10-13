package gov.nist.healthcare.mu.edos.custom;

import hl7.v2.instance.Simple;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import scala.collection.immutable.List;

public class SNOMED_CT extends CodedElementFormat {

    private static Logger logger = Logger.getLogger(SNOMED_CT.class.getName());

    private static String codeSystemValue = "SCT";

    private static Map<String, String> shortFormatPartitionIdenfifiers;
    private static Map<String, String> longFormatPartitionIdenfifiers;

    private static int[][] D5;
    private static int[][] F;

    static {
        shortFormatPartitionIdenfifiers = new HashMap<String, String>();
        shortFormatPartitionIdenfifiers.put("00", "A Concept");
        shortFormatPartitionIdenfifiers.put("01", "A Description");
        shortFormatPartitionIdenfifiers.put("02", "A Relationship");

        longFormatPartitionIdenfifiers = new HashMap<String, String>();
        longFormatPartitionIdenfifiers.put("10", "A Concept");
        longFormatPartitionIdenfifiers.put("11", "A Description");
        longFormatPartitionIdenfifiers.put("12", "A Relationship");

        D5 = new int[][]{ { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 },
                { 1, 2, 3, 4, 0, 6, 7, 8, 9, 5 },
                { 2, 3, 4, 0, 1, 7, 8, 9, 5, 6 },
                { 3, 4, 0, 1, 2, 8, 9, 5, 6, 7 },
                { 4, 0, 1, 2, 3, 9, 5, 6, 7, 8 },
                { 5, 9, 8, 7, 6, 0, 4, 3, 2, 1 },
                { 6, 5, 9, 8, 7, 1, 0, 4, 3, 2 },
                { 7, 6, 5, 9, 8, 2, 1, 0, 4, 3 },
                { 8, 7, 6, 5, 9, 3, 2, 1, 0, 4 },
                { 9, 8, 7, 6, 5, 4, 3, 2, 1, 0 } };

        F = new int[][]{ { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 },
                { 1, 5, 7, 6, 2, 8, 3, 0, 9, 4 },
                { 5, 8, 0, 3, 7, 9, 6, 1, 4, 2 },
                { 8, 9, 1, 6, 0, 4, 3, 5, 2, 7 },
                { 9, 4, 5, 3, 1, 2, 6, 8, 7, 0 },
                { 4, 2, 8, 6, 5, 7, 3, 9, 0, 1 },
                { 2, 7, 9, 3, 8, 0, 6, 4, 1, 5 },
                { 7, 0, 4, 6, 9, 1, 3, 2, 5, 8 } };
    }

    public String getCodeSystemValue() {
        return codeSystemValue;
    }

    /**
     * Check if a code is a valid SNOMED CT
     * 
     * @param code
     * @return
     */
    public boolean isValid(String code) {

        /*
         * The permissible value for the SCTIDs are limited by the following
         * rules:
         */
        /*
         * • Only positive integer values that are greater than 10^5 and less
         * than 10^18 are permitted.
         */
        /*
         * • The only valid string renderings of the Identifier value are
         * strings of decimal digits (0-9), commencing with a non zero digit.
         */
        /*
         * • The second and third digits from the right hand end of the string
         * rendering of the Identifier must match one of the
         * partition-identifier values specified in this guide.
         */
        /*
         * • The rightmost digit of the string rendering is a check-digit and
         * must match the value calculated using the specified check-digit
         * computation.
         */

        // TODO log error
        if (code.length() < 6 || code.length() > 18) {
            logger.error(String.format("Invalid length [%s] for code ''",
                    code.length(), code));
            return false;
        }

        if (!code.matches("[1-9]\\d+")) {
            logger.error(String.format("Invalid leading '0' for code %s", code));
            return false;
        }

        // partition-identifier
        boolean longFormat = false;
        String partitionIdentifier = StringUtils.right(
                StringUtils.left(code, code.length() - 1), 2);
        if (shortFormatPartitionIdenfifiers.keySet().contains(
                partitionIdentifier)) {
            longFormat = false;
        } else if (longFormatPartitionIdenfifiers.keySet().contains(
                partitionIdentifier)) {
            longFormat = true;
        } else {
            logger.error(String.format(
                    "Invalid partition identifier '%s' for code '%s'",
                    partitionIdentifier, code));
            return false;
        }

        if (longFormat) {
            // code length must be at least 11 (item (min 1) + namespace
            // (7) + partition (2) + check digit (1))
            if (code.length() < 11) {
                logger.error(String.format(
                        "Invalid length [%s] for long format code '%s'",
                        code.length(), code));
                return false;
            }
        }

        // check-digit
        String checkDigit = StringUtils.right(code, 1);
        int checkSum = verhoeffDihedralGroupD5Check(code);
        if (checkSum != 0) {
            logger.error(String.format(
                    "Invalid check digit '%s' for code '%s'", checkDigit, code));
            return false;
        }
        return true;
    }

    private int verhoeffDihedralGroupD5Check(String code) {
        String[] reversedCode = StringUtils.split(StringUtils.reverse(code),
                '.');
        int check = 0;
        for (int position = 0; position < reversedCode.length - 1; position++) {
            int digit = Integer.parseInt(reversedCode[position]);
            check = D5[check][F[position][digit]];
        }
        return check;
    }

    private String getValue(List<Simple> simpleElementList) {
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

    public static void main(String[] args) {
        // test with some valid codes
        // System.out.println(SNOMED_CT.isValid("100005"));
        // System.out.println(SNOMED_CT.isValid("999999990989121104"));
        // System.out.println(SNOMED_CT.isValid("9940000001126"));
        // System.out.println(SNOMED_CT.isValid("1290000001117"));
        // System.out.println(SNOMED_CT.isValid("1290989121103"));
        // System.out.println(SNOMED_CT.isValid("10989121108"));
        // System.out.println(SNOMED_CT.isValid("10000001105"));
        // System.out.println(SNOMED_CT.isValid("9940000001029"));
        // System.out.println(SNOMED_CT.isValid("1290023401015"));
        // System.out.println(SNOMED_CT.isValid("1290023401004"));
        // System.out.println(SNOMED_CT.isValid("100022"));
        // System.out.println(SNOMED_CT.isValid("100014"));
    }
}
