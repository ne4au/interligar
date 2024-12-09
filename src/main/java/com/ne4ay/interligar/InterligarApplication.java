package com.ne4ay.interligar;

import com.ne4ay.interligar.capture.ScreenCapturer;
import com.ne4ay.interligar.presenter.MainPresenter;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;

public class InterligarApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        ScreenCapturer screenCapturer = new ScreenCapturer();
        MainPresenter mainPresenter = new MainPresenter(screenCapturer);
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