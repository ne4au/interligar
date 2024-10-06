package com.ne4ay.interligar;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public final class FieldValidationRules {
    private FieldValidationRules() {}

    @Nonnull
    public static final Predicate<String> MUST_CONTAIN_ONLY_NUMBERS = text -> {
        for (var character : text.toCharArray()) {
            if (!Character.isDigit(character)) {
                return false;
            }
        }
        return true;
    };

    @Nonnull
    public static Predicate<String> LENGTH_LESSER_THEN(int size) {
        return text -> text.length() < size;
    }
}
