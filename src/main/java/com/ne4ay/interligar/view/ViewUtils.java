package com.ne4ay.interligar.view;

import com.ne4ay.interligar.utils.BorderStrokeBuilder;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;

import javax.annotation.Nonnull;

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

    @Nonnull
    public static <T extends Node> T addClassName(@Nonnull T node, @Nonnull String className) {
        node.getStyleClass().add(className);
        return node;
    }

    @Nonnull
    public static <T extends Node> T addClassNames(@Nonnull T node, @Nonnull String ... classNames) {
        for (var className : classNames) {
            addClassName(node, className);
        }
        return node;
    }

    @Nonnull
    public static <T extends Node> T removeClassName(@Nonnull T node, @Nonnull String className) {
        node.getStyleClass().remove(className);
        return node;
    }

    @Nonnull
    public static <T extends Node> T removeClassNames(@Nonnull T node, @Nonnull String ... classNames) {
        for (var className : classNames) {
            removeClassName(node, className);
        }
        return node;
    }

    @Nonnull
    public static <T extends Pane> T addChildren(@Nonnull T pane, @Nonnull Node ... nodes) {
        ObservableList<Node> children = pane.getChildren();
        for (var node : nodes) {
            children.add(node);
        }
        return pane;
    }
}
