package com.ne4ay.interligar.messages.data;

import com.ne4ay.interligar.data.MouseLocationDelta;
import com.ne4ay.interligar.messages.MessageData;

public record MouseChangePositionMessageData(double delX, double delY) implements MessageData {

    public static MouseChangePositionMessageData fromDelta(MouseLocationDelta delta) {
        return new MouseChangePositionMessageData(delta.delX(), delta.delY());
    }

    public MouseLocationDelta toDelta() {
        return new MouseLocationDelta(delX(), delY());
    }
}
