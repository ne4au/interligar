package com.ne4ay.interligar.messages;

import static com.ne4ay.interligar.messages.MessageType.TEST;

public record Message<T extends MessageData>(MessageType messageType,
                                             T messageData)
{
    public static Message<TestMessageData> createTestMessage(String text) {
        return new Message<>(TEST, new TestMessageData(text));
    }
}
