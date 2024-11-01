package com.ne4ay.interligar.view;

import com.ne4ay.interligar.ValidatedField;
import com.ne4ay.interligar.utils.ViewBuilder;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.ne4ay.interligar.FieldValidationRules.IS_ACCEPTABLE_IP_PART_INPUT;
import static com.ne4ay.interligar.FieldValidationRules.IS_ACCEPTABLE_PORT_INPUT;
import static com.ne4ay.interligar.utils.ViewNodeBuilder.builder;
import static com.ne4ay.interligar.view.ViewUtils.addChildren;
import static com.ne4ay.interligar.view.ViewUtils.addChildrenViews;
import static javafx.geometry.Pos.CENTER_LEFT;
import static javafx.geometry.Pos.TOP_CENTER;

public class ClientConfView implements View<VBox> {

    private final VBox root;
    private final Label titleText;
    private final HBox serverAddressLine;

    private final List<ValidatedField> addressFields;
    private final Label colonLabel;
    private final ValidatedField portField;

    private final Label clientInfoText;
    private final Button startClientButton;

    private volatile Runnable startClientButtonListener = () -> {};

    public ClientConfView() {
        this.root = createRoot();
        this.titleText = new Label("Client configurations");
        this.serverAddressLine = createClientAddressLine();
        this.colonLabel = new Label(":");
        this.addressFields = IntStream.range(0, 4)
            .mapToObj(__ -> createIpAddressPartField())
            .toList();
        this.portField = createPortField();
        addChildrenViews(serverAddressLine, addressFields);
        addChildren(serverAddressLine, colonLabel);
        addChildren(serverAddressLine, portField);


        this.clientInfoText = new Label("Client is not running");
        this.startClientButton = createStartClientButton();

        addChildren(root,
            titleText, serverAddressLine, clientInfoText, startClientButton);
    }

    public String getAddressString() {
        return this.addressFields.stream()
            .map(ValidatedField::getText)
            .collect(Collectors.joining("."));
    }

    public String getPortString() {
        return this.portField.getText();
    }

    public ClientConfView setClientInfoText(String text) {
        this.clientInfoText.setText(text);
        return this;
    }

    public ClientConfView setStartClientButtonListener(Runnable startClientButtonListener) {
        this.startClientButtonListener = startClientButtonListener;
        return this;
    }

    public ClientConfView setStartClientButtonText(String text) {
        this.startClientButton.setText(text);
        return this;
    }

    private ValidatedField createIpAddressPartField() {
        return ViewBuilder.builder(new ValidatedField(IS_ACCEPTABLE_IP_PART_INPUT))
            .set(TextField::maxWidth, 55.)
            .set(TextField::setPrefWidth, 55.)
            .build();
    }

    private ValidatedField createPortField() {
        return ViewBuilder.builder(new ValidatedField(new TextField(), IS_ACCEPTABLE_PORT_INPUT))
            .set(TextField::maxWidth, 100.)
            .set(TextField::setPrefWidth, 100.)
            .build();
    }

    private Button createStartClientButton() {
        return builder(new Button("Start client"))
            .set(Button::setOnAction, event -> this.startClientButtonListener.run())
            .build();
    }


    private HBox createClientAddressLine() {
        return builder(new HBox())
            .set(HBox::setAlignment, CENTER_LEFT)
            .set(HBox::setSpacing, 5.)
            .set(HBox::setPadding, new Insets(5., 0., 0., 0.))
            .build();
    }

    private VBox createRoot() {
        return builder(new VBox())
            .set(VBox::setAlignment, TOP_CENTER)
            .set(VBox::setSpacing, 5.)
            .build();
    }

    @Override
    public VBox getAsNode() {
        return root;
    }
}
