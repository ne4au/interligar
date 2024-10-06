package com.ne4ay.interligar;

import javafx.scene.control.TextField;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class ValidatedField {
    private final TextField field;
    private final Predicate<String> predicate;
    private volatile String previousState;

    public ValidatedField(@Nonnull TextField field, @Nonnull Predicate<String> predicate) {
        this.field = field;
        this.predicate = predicate;
        this.previousState = field.getText();
    }

    public void onKeyTyped() {
        String text = this.field.getText();
        if (predicate.test(text)) {
            this.previousState = text;
            return;
        }
        this.field.setText(this.previousState);
    }
}
