package com.ne4ay.interligar.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MessageModel(@JsonProperty String id, @JsonProperty MessageData data) {

    public static MessageModel fromMessage(Message<?> message) {
        return new MessageModel(
            message.messageType().getId(),
            message.messageData());
    }
}
