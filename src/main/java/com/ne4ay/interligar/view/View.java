package com.ne4ay.interligar.view;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

public interface View<T extends Node> {

    T getAsNode();

    default void addToRoot(Pane pane) {
        pane.getChildren().add(getAsNode());
    }
}
