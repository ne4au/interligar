package com.ne4ay.interligar.presenter;

import com.ne4ay.interligar.udp.ChannelState;
import com.ne4ay.interligar.view.MainView;
import javafx.scene.layout.VBox;

import javax.annotation.Nonnull;

public class MainPresenter {

    private final MainView mainView;

    private final ServerConfPresenter serverConfPresenter;
    private final ClientConfPresenter clientConfPresenter;
    private final ChannelState channelState;

    public MainPresenter() {
        this.mainView = new MainView();

        this.channelState = new ChannelState();
        this.serverConfPresenter = new ServerConfPresenter(this.mainView.getServerConfView(), channelState);
        this.clientConfPresenter = new ClientConfPresenter(this.mainView.getClientConfView(), channelState);
    }

    @Nonnull
    public VBox getRoot() {
        return mainView.getRoot();
    }
}
