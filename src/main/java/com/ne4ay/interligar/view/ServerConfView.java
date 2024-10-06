package com.ne4ay.interligar.view;

import com.ne4ay.interligar.ValidatedField;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.annotation.Nonnull;

import static com.ne4ay.interligar.FieldValidationRules.LENGTH_LESSER_THEN;
import static com.ne4ay.interligar.FieldValidationRules.MUST_CONTAIN_ONLY_NUMBERS;
import static com.ne4ay.interligar.utils.InterligarUtils.DEFAULT_PORT;
import static com.ne4ay.interligar.utils.ViewNodeBuilder.builder;
import static com.ne4ay.interligar.view.ViewUtils.addChildren;
import static javafx.geometry.Pos.CENTER_LEFT;
import static javafx.geometry.Pos.TOP_CENTER;

public class ServerConfView implements View {

    private final VBox root;
    private final Label titleText;
    private final HBox serverAddressLine;

    private final Label ipText;
    private final TextField portField;
    private final ValidatedField portValidatedField;

    private final Label serverInfoText;
    private final Button startServerButton;

    private volatile Runnable startServerButtonListener = () -> {};

    public ServerConfView() {
        this.root = createRoot();
        this.titleText = new Label("Server configurations");
        this.serverAddressLine = createServerAddressLine();

        this.ipText = new Label();
        this.portField = new TextField(DEFAULT_PORT);
        addChildren(serverAddressLine,
            ipText, portField);
        this.portValidatedField = new ValidatedField(portField, MUST_CONTAIN_ONLY_NUMBERS.and(LENGTH_LESSER_THEN(6)));
        confPortField(portField);
        this.serverInfoText = new Label("Server is not running");
        this.startServerButton = createStartServerButton();

        addChildren(root,
            titleText, serverAddressLine, serverInfoText, startServerButton);
    }

    @Nonnull
    public String  getPortString() {
        return portField.getText();
    }

    public ServerConfView setStartServerButtonListener(Runnable startServerButtonListener) {
        this.startServerButtonListener = startServerButtonListener;
        return this;
    }

    public void setStartServerButtonText(@Nonnull String text) {
        this.startServerButton.setText(text);
    }

    public void setServerInfoText(@Nonnull String text) {
        this.serverInfoText.setText(text);
    }

    public void setIpText(@Nonnull String text) {
        this.ipText.setText(text);
    }

    @Nonnull
    private HBox createServerAddressLine() {
        return builder(new HBox())
            .set(HBox::setAlignment, CENTER_LEFT)
            .set(HBox::setSpacing, 5.)
            .set(HBox::setPadding, new Insets(5., 0., 0., 0.))
            .build();
    }

    @Nonnull
    private TextField confPortField(TextField field) {
        return builder(field)
            .set(TextField::maxWidth, 100.)
            .set(TextField::setOnKeyTyped, event -> this.portValidatedField.onKeyTyped())
            .build();
    }

    @Nonnull
    private Button createStartServerButton() {
        return builder(new Button("Start server"))
            .set(Button::setOnAction, event -> this.startServerButtonListener.run())
            .build();
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
