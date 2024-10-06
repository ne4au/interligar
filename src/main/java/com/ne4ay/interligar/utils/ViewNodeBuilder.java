package com.ne4ay.interligar.utils;

import com.ne4ay.interligar.view.ViewUtils;
import javafx.scene.Node;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;

public record ViewNodeBuilder<T extends Node>(@Nonnull T elem) {

    public static <T extends Node> ViewNodeBuilder<T> builder(@Nonnull T elem) {
        return new ViewNodeBuilder<>(elem);
    }

    public <A> ViewNodeBuilder<T> set(@Nonnull BiConsumer<T, A> setter, @Nonnull A arg) {
        setter.accept(elem(), arg);
        return this;
    }

    public ViewNodeBuilder<T> addClassName(@Nonnull String className) {
        ViewUtils.addClassName(elem(), className);
        return this;
    }

    public ViewNodeBuilder<T> addClassNames(@Nonnull String ... classNames) {
        ViewUtils.addClassNames(elem(), classNames);
        return this;
    }

    public ViewNodeBuilder<T> removeClassName(@Nonnull String className) {
        ViewUtils.removeClassName(elem(), className);
        return this;
    }

    public ViewNodeBuilder<T> removeClassNames(@Nonnull String ... classNames) {
        ViewUtils.removeClassNames(elem(), classNames);
        return this;
    }

    @Nonnull
    public T build() {
        return elem();
    }
}
