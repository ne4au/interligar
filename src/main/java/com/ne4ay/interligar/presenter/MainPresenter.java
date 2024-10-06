package com.ne4ay.interligar.presenter;

import com.ne4ay.interligar.view.MainView;
import javafx.scene.layout.VBox;

import javax.annotation.Nonnull;

public class MainPresenter {

    private final MainView mainView;

    private final ServerConfPresenter serverConfPresenter;
    private final ClientConfPresenter clientConfPresenter;

    public MainPresenter() {
        this.mainView = new MainView();

        this.serverConfPresenter = new ServerConfPresenter(this.mainView.getServerConfView());
        this.clientConfPresenter = new ClientConfPresenter(this.mainView.getClientConfView());
    }

    @Nonnull
    public VBox getRoot() {
        return mainView.getRoot();
    }
}