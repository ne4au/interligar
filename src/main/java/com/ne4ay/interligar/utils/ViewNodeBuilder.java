package com.ne4ay.interligar.utils;

import com.ne4ay.interligar.view.ViewUtils;
import javafx.scene.Node;

import java.util.function.BiConsumer;

public record ViewNodeBuilder<T extends Node>(T elem) {

    public static <T extends Node> ViewNodeBuilder<T> builder(T elem) {
        return new ViewNodeBuilder<>(elem);
    }

    public <A> ViewNodeBuilder<T> set(BiConsumer<T, A> setter, A arg) {
        setter.accept(elem(), arg);
        return this;
    }

    public ViewNodeBuilder<T> addClassName(String className) {
        ViewUtils.addClassName(elem(), className);
        return this;
    }

    public ViewNodeBuilder<T> addClassNames(String ... classNames) {
        ViewUtils.addClassNames(elem(), classNames);
        return this;
    }

    public ViewNodeBuilder<T> removeClassName(String className) {
        ViewUtils.removeClassName(elem(), className);
        return this;
    }

    public ViewNodeBuilder<T> removeClassNames(String ... classNames) {
        ViewUtils.removeClassNames(elem(), classNames);
        return this;
    }

    public T build() {
        return elem();
    }
}
