package com.ne4ay.interligar.utils;

import javafx.geometry.Insets;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;

import javax.annotation.Nonnull;

public class BorderStrokeBuilder {
    private Paint topStroke;
    private Paint rightStroke;
    private Paint bottomStroke;
    private Paint leftStroke;
    private BorderStrokeStyle topStyle;
    private BorderStrokeStyle rightStyle;
    private BorderStrokeStyle bottomStyle;
    private BorderStrokeStyle leftStyle;
    private CornerRadii radii;
    private BorderWidths widths;
    private Insets insets;

    public BorderStrokeBuilder setTopStroke(Paint topStroke) {
        this.topStroke = topStroke;
        return this;
    }

    public BorderStrokeBuilder setRightStroke(Paint rightStroke) {
        this.rightStroke = rightStroke;
        return this;
    }

    public BorderStrokeBuilder setBottomStroke(Paint bottomStroke) {
        this.bottomStroke = bottomStroke;
        return this;
    }

    public BorderStrokeBuilder setLeftStroke(Paint leftStroke) {
        this.leftStroke = leftStroke;
        return this;
    }

    public BorderStrokeBuilder setTopStyle(BorderStrokeStyle topStyle) {
        this.topStyle = topStyle;
        return this;
    }

    public BorderStrokeBuilder setRightStyle(BorderStrokeStyle rightStyle) {
        this.rightStyle = rightStyle;
        return this;
    }

    public BorderStrokeBuilder setBottomStyle(BorderStrokeStyle bottomStyle) {
        this.bottomStyle = bottomStyle;
        return this;
    }

    public BorderStrokeBuilder setLeftStyle(BorderStrokeStyle leftStyle) {
        this.leftStyle = leftStyle;
        return this;
    }

    public BorderStrokeBuilder setRadii(CornerRadii radii) {
        this.radii = radii;
        return this;
    }

    public BorderStrokeBuilder setWidths(BorderWidths widths) {
        this.widths = widths;
        return this;
    }

    public BorderStrokeBuilder setInsets(Insets insets) {
        this.insets = insets;
        return this;
    }

    @Nonnull
    public BorderStrokeBuilder setTopBorder(Paint topStroke, BorderStrokeStyle topStyle) {
        return setTopStroke(topStroke)
            .setTopStyle(topStyle);
    }

    @Nonnull
    public BorderStrokeBuilder setRightBorder(Paint rightStroke, BorderStrokeStyle rightStyle) {
        return setRightStroke(rightStroke)
            .setRightStyle(rightStyle);
    }

    @Nonnull
    public BorderStrokeBuilder setBottomBorder(Paint bottomStroke, BorderStrokeStyle bottomStyle) {
        return setBottomStroke(bottomStroke)
            .setBottomStyle(bottomStyle);
    }

    @Nonnull
    public BorderStrokeBuilder setLeftBorder(Paint leftStroke, BorderStrokeStyle leftStyle) {
        return setLeftStroke(leftStroke)
            .setLeftStyle(leftStyle);
    }

    @Nonnull
    public BorderStroke build() {
        return new BorderStroke(
            topStroke, rightStroke, bottomStroke, leftStroke,
            topStyle, rightStyle, bottomStyle, leftStyle,
            radii, widths, insets);
    }
}
