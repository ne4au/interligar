package com.ne4ay.interligar.utils;

import com.ne4ay.interligar.view.View;
import com.ne4ay.interligar.view.ViewUtils;
import javafx.scene.Node;

import java.util.function.BiConsumer;

public record ViewBuilder<N extends Node, T extends View<N>>(T elem) {

    public static <N extends Node, T extends View<N>> ViewBuilder<N, T> builder(T elem) {
        return new ViewBuilder<>(elem);
    }

    public <A> ViewBuilder<N, T> set(BiConsumer<N, A> setter, A arg) {
        setter.accept(elem().getAsNode(), arg);
        return this;
    }

    public ViewBuilder<N, T> addClassName(String className) {
        ViewUtils.addClassName(elem().getAsNode(), className);
        return this;
    }

    public ViewBuilder<N, T> addClassNames(String ... classNames) {
        ViewUtils.addClassNames(elem().getAsNode(), classNames);
        return this;
    }

    public  ViewBuilder<N, T> removeClassName(String className) {
        ViewUtils.removeClassName(elem().getAsNode(), className);
        return this;
    }

    public  ViewBuilder<N, T> removeClassNames(String ... classNames) {
        ViewUtils.removeClassNames(elem().getAsNode(), classNames);
        return this;
    }

    public T build() {
        return elem();
    }
}
