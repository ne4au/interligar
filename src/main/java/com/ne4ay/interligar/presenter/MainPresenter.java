package com.ne4ay.interligar.presenter;

import com.ne4ay.interligar.capture.ScreenCapturer;
import com.ne4ay.interligar.view.MainView;
import javafx.scene.layout.VBox;

public class MainPresenter {

    private final MainView mainView;
    private final ServerConfPresenter serverConfPresenter;
    private final ClientConfPresenter clientConfPresenter;
    private final ScreenCapturer screenCapturer;

    public MainPresenter(ScreenCapturer screenCapturer) {
        this.mainView = new MainView();

        this.screenCapturer = new ScreenCapturer();
        this.serverConfPresenter = new ServerConfPresenter(this.mainView.getServerConfView(), screenCapturer);
        this.clientConfPresenter = new ClientConfPresenter(this.mainView.getClientConfView(), screenCapturer);
    }

    public VBox getRoot() {
        return mainView.getRoot();
    }
}
