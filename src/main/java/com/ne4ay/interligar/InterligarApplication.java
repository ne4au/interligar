package com.ne4ay.interligar;

import com.ne4ay.interligar.presenter.MainPresenter;
import com.ne4ay.interligar.view.MainView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InterligarApplication extends Application {
    public static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();

    @Override
    public void start(Stage stage) throws IOException, URISyntaxException {
        MainPresenter mainPresenter = new MainPresenter();
        Scene scene = new Scene(mainPresenter.getRoot(), 600, 600);
        scene.getStylesheets().add(InterligarApplication.class.getResource("main-view.css").toURI().toString());
        stage.setTitle("Interligar");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}