package com.ne4ay.interligar.messages;

import com.ne4ay.interligar.messages.data.ChangeDestinationModeRequestMessageData;
import com.ne4ay.interligar.messages.data.ChangeDestinationModeResponseMessageData;
import com.ne4ay.interligar.messages.data.TestMessageData;

import static com.ne4ay.interligar.messages.MessageType.CHANGE_DESTINATION_REQUEST;
import static com.ne4ay.interligar.messages.MessageType.CHANGE_DESTINATION_RESPONSE;
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
}
