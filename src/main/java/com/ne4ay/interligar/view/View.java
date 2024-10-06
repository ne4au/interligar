package com.ne4ay.interligar.view;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

import javax.annotation.Nonnull;

public interface View {

    @Nonnull
    Node getAsNode();

    default void addToRoot(@Nonnull Pane pane) {
        pane.getChildren().add(getAsNode());
    }
}
