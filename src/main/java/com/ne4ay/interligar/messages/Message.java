package com.ne4ay.interligar.messages;

import com.ne4ay.interligar.data.MouseLocationDelta;
import com.ne4ay.interligar.messages.data.ChangeDestinationModeRequestMessageData;
import com.ne4ay.interligar.messages.data.ChangeDestinationModeResponseMessageData;
import com.ne4ay.interligar.messages.data.MouseChangePositionMessageData;
import com.ne4ay.interligar.messages.data.TestMessageData;

import static com.ne4ay.interligar.messages.MessageType.CHANGE_DESTINATION_REQUEST;
import static com.ne4ay.interligar.messages.MessageType.CHANGE_DESTINATION_RESPONSE;
import static com.ne4ay.interligar.messages.MessageType.MOUSE_CHANGE_POSITION;
import static com.ne4ay.interligar.messages.MessageType.TEST;

public record Message<T extends MessageData>(MessageType<T> messageType,
                                             T messageData)
{
    public static Message<TestMessageData> createTestMessage(String text) {
        return new Message<>(TEST, new TestMessageData(text));
    }

    public static Message<ChangeDestinationModeRequestMessageData> createChangeDestinationRequestMessage() {
        return new Message<>(CHANGE_DESTINATION_REQUEST, new ChangeDestinationModeRequestMessageData());
    }

    public static Message<ChangeDestinationModeResponseMessageData> createChangeDestinationResponseMessage(boolean isSuccessful) {
        return new Message<>(CHANGE_DESTINATION_RESPONSE, new ChangeDestinationModeResponseMessageData(isSuccessful));
    }

    public static Message<MouseChangePositionMessageData> createMouseChangePositionMessage(int delX, int delY) {
        return new Message<>(MOUSE_CHANGE_POSITION, new MouseChangePositionMessageData(delX, delY));
    }

    public static Message<MouseChangePositionMessageData> createMouseChangePositionMessage(MouseLocationDelta delta) {
        return new Message<>(MOUSE_CHANGE_POSITION, MouseChangePositionMessageData.fromDelta(delta));
    }
}
