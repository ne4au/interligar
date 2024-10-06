package com.ne4ay.interligar.controllers;

import com.ne4ay.interligar.ValidatedField;
import com.ne4ay.interligar.udp.UDPServer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.CompletableFuture;

import static com.ne4ay.interligar.FieldValidationRules.LENGTH_LESSER_THEN;
import static com.ne4ay.interligar.FieldValidationRules.MUST_CONTAIN_ONLY_NUMBERS;
import static com.ne4ay.interligar.utils.InterligarUtils.wrapInPlatformCall;

public class MainController {


    @FXML private Button serverButton;
    @FXML private VBox serverPanel;
    @FXML private Label ipText;
    @FXML private TextField portField;
    private ValidatedField portValidatedField;
    @FXML private Label serverInfoText;
    @FXML private Button startServerButton;


    @FXML private Button clientButton;
    @FXML public VBox clientPanel;
    @FXML private Label titleText;

    private volatile CompletableFuture<Void> udpChannelFuture = CompletableFuture.completedFuture(null);

    @FXML
    public void initialize() throws UnknownHostException {
        this.portField.setText("24000");
        this.portValidatedField = new ValidatedField(portField, MUST_CONTAIN_ONLY_NUMBERS.and(LENGTH_LESSER_THEN(6)));
        this.ipText.setText("Address:    " + InetAddress.getLocalHost().getHostAddress() + ":");
        this.clientPanel.setVisible(false);
    }

    @FXML
    protected void onServerButtonClick() {
        this.serverPanel.setVisible(false);
    }

    @FXML
    protected void onClientButtonClick() {

    }

    @FXML
    protected void onStartServerButtonClick() {
        startServer();
    }

    private void shutDownServer() {

    }

    private void startServer() {

    }



    @FXML
    protected void onPortFieldKeyTyped() {
        this.portValidatedField.onKeyTyped();
    }
}