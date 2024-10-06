package com.ne4ay.interligar.presenter;

import com.ne4ay.interligar.view.ClientConfView;

import javax.annotation.Nonnull;

public class ClientConfPresenter {

    private final ClientConfView view;

    public ClientConfPresenter(@Nonnull ClientConfView view) {
        this.view = view;
    }
}
