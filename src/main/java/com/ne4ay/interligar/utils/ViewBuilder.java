package com.ne4ay.interligar.utils;

import com.ne4ay.interligar.view.View;
import com.ne4ay.interligar.view.ViewUtils;
import javafx.scene.Node;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;

public record ViewBuilder<N extends Node, T extends View<N>>(@Nonnull T elem) {

    @Nonnull
    public static <N extends Node, T extends View<N>> ViewBuilder<N, T> builder(@Nonnull T elem) {
        return new ViewBuilder<>(elem);
    }

    @Nonnull
    public <A> ViewBuilder<N, T> set(@Nonnull BiConsumer<N, A> setter, @Nonnull A arg) {
        setter.accept(elem().getAsNode(), arg);
        return this;
    }

    public ViewBuilder<N, T> addClassName(@Nonnull String className) {
        ViewUtils.addClassName(elem().getAsNode(), className);
        return this;
    }

    public ViewBuilder<N, T> addClassNames(@Nonnull String ... classNames) {
        ViewUtils.addClassNames(elem().getAsNode(), classNames);
        return this;
    }

    public  ViewBuilder<N, T> removeClassName(@Nonnull String className) {
        ViewUtils.removeClassName(elem().getAsNode(), className);
        return this;
    }

    public  ViewBuilder<N, T> removeClassNames(@Nonnull String ... classNames) {
        ViewUtils.removeClassNames(elem().getAsNode(), classNames);
        return this;
    }

    @Nonnull
    public T build() {
        return elem();
    }
}
