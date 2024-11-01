package com.ne4ay.interligar;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class FieldValidationRules {
    private FieldValidationRules() {}

    public static final Predicate<String> MUST_CONTAIN_ONLY_NUMBERS = text -> {
        for (var character : text.toCharArray()) {
            if (!Character.isDigit(character)) {
                return false;
            }
        }
        return true;
    };


    public static final Predicate<String> IS_ACCEPTABLE_PORT_INPUT =
        MUST_CONTAIN_ONLY_NUMBERS.and(LENGTH_LESSER_THEN(6));

    public static final Predicate<String> IS_IP_ADDRESS = text -> {
        if (!text.contains(".")) {
            return false;
        }
        String[] parts = text.split("\\.");
        if (parts.length < 4) {
            return false;
        }
        return Arrays.stream(parts)
            .allMatch(FieldValidationRules::isValidAddressPart);
    };

    public static final Predicate<String> IS_ACCEPTABLE_IP_PART_INPUT = text -> {
        try {
            int intRep = Integer.parseInt(text);
            return intRep >= 0 && intRep <= 255;
        } catch (Exception e) {
            return false;
        }
    };

    public static final Predicate<String> IS_ACCEPTABLE_IP_ADDRESS_INPUT = text -> {
        if (text.isEmpty()) {
            return true;
        }
        int dotCount = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '.') dotCount++;
        }
        if (dotCount == 0) {
            if (text.length() > 3) {
                return false;
            }
            return isValidAddressPart(text);
        }
        if (dotCount > 3) {
            return false;
        }
        if (dotCount == 1 && text.length() == 1) {
            return false;
        }
        String[] parts = text.split("\\.");
        if (dotCount > parts.length) {
            return false;
        }
        return Arrays.stream(parts)
            .allMatch(FieldValidationRules::isValidAddressPart);
    };

    private static boolean isValidAddressPart( String part) {
        try {
            int addressPart = Integer.parseInt(part);
            if (addressPart < 0 || addressPart > 255) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    
    public static Predicate<String> LENGTH_LESSER_THEN(int size) {
        return text -> text.length() < size;
    }
}
