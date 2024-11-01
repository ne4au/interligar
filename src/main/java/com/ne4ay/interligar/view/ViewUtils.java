package com.ne4ay.interligar.view;

import com.ne4ay.interligar.utils.BorderStrokeBuilder;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static javafx.scene.layout.BorderStrokeStyle.SOLID;
import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.GAINSBORO;
import static javafx.scene.paint.Color.LIGHTGREEN;
import static javafx.scene.paint.Color.LIGHTGREY;
import static javafx.scene.paint.Color.TRANSPARENT;
import static javafx.scene.paint.Color.WHITE;

public final class ViewUtils {
    private ViewUtils() {}

    public static final Insets PADDING_5 = new Insets(5., 5., 5., 5.);
    public static final Border ACTIVE_TAB_BORDER = new Border(new BorderStrokeBuilder()
        .setLeftBorder(BLACK, SOLID)
        .setTopBorder(BLACK, SOLID)
        .setRightBorder(BLACK, SOLID)
        .setBottomBorder(TRANSPARENT, SOLID)
        .build());

    public static final Border INACTIVE_TAB_BORDER = new Border(new BorderStrokeBuilder()
        .setLeftBorder(BLACK, SOLID)
        .setTopBorder(BLACK, SOLID)
        .setRightBorder(BLACK, SOLID)
        .setBottomBorder(BLACK, SOLID)
        .build());

    public static final Background WHITE_BACKGROUND = new Background(new BackgroundFill(WHITE, null, null));
    public static final Background LIGHT_GRAY_BACKGROUND = new Background(new BackgroundFill(LIGHTGREY, null, null));

    public static <T extends Node> T addClassName(T node, String className) {
        node.getStyleClass().add(className);
        return node;
    }

    public static <T extends Node> T addClassNames(T node, String... classNames) {
        for (var className : classNames) {
            addClassName(node, className);
        }
        return node;
    }

    public static <T extends Node> T removeClassName(T node, String className) {
        node.getStyleClass().remove(className);
        return node;
    }

    public static <T extends Node> T removeClassNames(T node, String... classNames) {
        for (var className : classNames) {
            removeClassName(node, className);
        }
        return node;
    }

    public static <T extends Pane> T addChildren(T pane, Node... nodes) {
        return addChildren(pane, Arrays.asList(nodes));
    }

    public static <T extends Pane> T addChildren(T pane, List<? extends Node> nodes) {
        pane.getChildren().addAll(nodes);
        return pane;
    }

    public static <T extends Pane> T addChildren(T pane, View... nodes) {
        return addChildrenViews(pane, Arrays.stream(nodes));
    }

    public static <T extends Pane> T addChildrenViews(T pane, List<? extends View> nodes) {
        return addChildrenViews(pane, nodes.stream());
    }


    public static <T extends Pane> T addChildrenViews(T pane, Stream<? extends View> nodes) {
        pane.getChildren().addAll(nodes.map(View::getAsNode).toList());
        return pane;
    }


    public static <T extends Pane> T removeChildren(T pane, Node... nodes) {
        pane.getChildren().removeAll(Arrays.asList(nodes));
        return pane;
    }


    public static <T extends Pane> T removeChildren(T pane, View... nodes) {
        pane.getChildren().removeAll(Arrays.stream(nodes).map(View::getAsNode).toList());
        return pane;
    }
}
