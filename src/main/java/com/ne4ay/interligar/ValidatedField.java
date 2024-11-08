package com.ne4ay.interligar;

import com.ne4ay.interligar.view.View;
import javafx.scene.Node;
import javafx.scene.control.TextField;

import java.util.function.Predicate;

public class ValidatedField implements View<TextField> {
    private final TextField field;
    private final Predicate<String> predicate;
    private volatile String previousState;

    public ValidatedField( TextField field,  Predicate<String> predicate) {
        this.field = field;
        this.field.setOnKeyTyped(event -> onKeyTyped());
        this.predicate = predicate;
        this.previousState = field.getText();
    }

    public ValidatedField( Predicate<String> predicate) {
        this(new TextField(), predicate);
    }

    public void onKeyTyped() {
        String text = this.field.getText();
        if (predicate.test(text)) {
            this.previousState = text;
            return;
        }
        this.field.setText(this.previousState);
        this.field.positionCaret(this.previousState.length());
    }

    
    public String getText() {
        return getAsNode().getText();
    }

    
    @Override
    public TextField getAsNode() {
        return field;
    }
}
