package com.ne4ay.interligar.view;

import javafx.scene.Node;
import javafx.scene.layout.VBox;

import javax.annotation.Nonnull;

import static com.ne4ay.interligar.utils.ViewNodeBuilder.builder;
import static com.ne4ay.interligar.view.ViewUtils.PADDING_5;
import static javafx.geometry.Pos.TOP_CENTER;

public class ClientConfView implements View {

    private final VBox root;

    public ClientConfView() {
        this.root = createRoot();
    }

    @Nonnull
    private VBox createRoot() {
        return builder(new VBox())
            .set(VBox::setAlignment, TOP_CENTER)
            .set(VBox::setSpacing, 5.)
            .build();
    }

    @Nonnull
    @Override
    public Node getAsNode() {
        return root;
    }
}
