package com.ne4ay.interligar.view;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.annotation.Nonnull;

import static com.ne4ay.interligar.utils.CssClasses.ACTIVE_TAB;
import static com.ne4ay.interligar.utils.CssClasses.ACTIVE_TAB_FOCUS;
import static com.ne4ay.interligar.utils.CssClasses.INACTIVE_TAB;
import static com.ne4ay.interligar.utils.ViewNodeBuilder.builder;
import static com.ne4ay.interligar.view.ViewUtils.ACTIVE_TAB_BORDER;
import static com.ne4ay.interligar.view.ViewUtils.INACTIVE_TAB_BORDER;
import static com.ne4ay.interligar.view.ViewUtils.LIGHT_GRAY_BACKGROUND;
import static com.ne4ay.interligar.view.ViewUtils.PADDING_5;
import static com.ne4ay.interligar.view.ViewUtils.WHITE_BACKGROUND;
import static com.ne4ay.interligar.view.ViewUtils.addChildren;
import static javafx.geometry.Pos.CENTER_LEFT;
import static javafx.geometry.Pos.TOP_CENTER;

public class MainView implements View {

    private final VBox root;
    private final HBox header;

    private final Button serverButton;
    private final Button clientButton;

    private final ServerConfView serverConfView;
    private final ClientConfView clientConfView;

    private volatile CurrentMode currentMode = CurrentMode.SERVER;

    public MainView() {
        this.root = createRoot();
        this.header = createHeader();
        this.root.getChildren().add(this.header);

        this.serverButton = createServerButton();
        this.clientButton = createClientButton();
        addChildren(header, serverButton, clientButton);

        this.serverConfView = createServerConfView();
        this.clientConfView = createClientConfView();
        addChildren(root, serverConfView.getAsNode(), clientConfView.getAsNode());
    }

    @Nonnull
    public ServerConfView getServerConfView() {
        return serverConfView;
    }

    @Nonnull
    public ClientConfView getClientConfView() {
        return clientConfView;
    }

    @Nonnull
    public VBox getRoot() {
        return root;
    }

    private void changeToServerMode() {
        if (this.currentMode == CurrentMode.SERVER) {
            return;
        }
        this.currentMode = CurrentMode.SERVER;
        setTabActive(this.serverButton);
        setTabInActive(this.clientButton);
        this.clientConfView.getAsNode().setVisible(false);
        this.serverConfView.getAsNode().setVisible(true);
    }

    private void changeToClientMode() {
        if (this.currentMode == CurrentMode.CLIENT) {
            return;
        }
        this.currentMode = CurrentMode.CLIENT;
        setTabActive(this.clientButton);
        setTabInActive(this.serverButton);
        this.serverConfView.getAsNode().setVisible(false);
        this.clientConfView.getAsNode().setVisible(true);
    }

    private void setTabActive(@Nonnull Button button) {
        button.setBorder(ACTIVE_TAB_BORDER);
        button.setBackground(WHITE_BACKGROUND);
        ViewUtils.removeClassName(button, INACTIVE_TAB);
        ViewUtils.addClassName(button, ACTIVE_TAB);
    }

    private void setTabInActive(@Nonnull Button button) {
        button.setBorder(INACTIVE_TAB_BORDER);
        button.setBackground(LIGHT_GRAY_BACKGROUND);
        ViewUtils.removeClassName(button, ACTIVE_TAB);
        ViewUtils.addClassName(button, INACTIVE_TAB);
    }

    private ServerConfView createServerConfView() {
        return new ServerConfView();
    }

    private ClientConfView createClientConfView() {
        return new ClientConfView();
    }

    @Nonnull
    private VBox createRoot() {
        return builder(new VBox())
            .set(VBox::setAlignment, TOP_CENTER)
            .set(VBox::setSpacing, 5.)
            .set(VBox::setPadding, PADDING_5)
            .build();
    }

    @Nonnull
    private HBox createHeader() {
        return builder(new HBox())
            .set(HBox::setAlignment, CENTER_LEFT)
            .set(HBox::setSpacing, 5.)
            .build();
    }

    @Nonnull
    private Button createServerButton() {
        return builder(new Button("Server mode"))
            .set(Button::setOnAction, actionEvent -> changeToServerMode())
            .set(Button::setBorder, ACTIVE_TAB_BORDER)
            .set(Button::setBackground, WHITE_BACKGROUND)
            .addClassNames(ACTIVE_TAB_FOCUS, ACTIVE_TAB)
            .build();
    }

    @Nonnull
    private Button createClientButton() {
        return builder(new Button("Client mode"))
            .set(Button::setOnAction, actionEvent -> changeToClientMode())
            .set(Button::setBorder, INACTIVE_TAB_BORDER)
            .set(Button::setBackground, LIGHT_GRAY_BACKGROUND)
            .addClassNames(ACTIVE_TAB_FOCUS, INACTIVE_TAB)
            .build();
    }

    @Nonnull
    @Override
    public Node getAsNode() {
        return this.root;
    }

    private enum CurrentMode {
        SERVER, CLIENT;
    }
}
