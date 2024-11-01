package com.ne4ay.interligar.messages;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public enum MessageType {
    TEST("test", TestMessageData.class)
    ;
    private static final Map<String, MessageType> MESSAGE_TYPE_MAP = Arrays.stream(values())
        .collect(Collectors.toMap(
            MessageType::getId,
            Function.identity()
        ));

    private final String id;
    private final Class<? extends MessageData> messageDataClass;

    MessageType(String id, Class<? extends MessageData> messageDataClass) {
        this.id = requireNonNull(id, "id");
        this.messageDataClass = requireNonNull(messageDataClass, "messageDataClass");
    }

    public static MessageType getMessageTypeForId(String id) {
        return MESSAGE_TYPE_MAP.get(id);
    }

    public static Optional<MessageType> getMessageTypeForIdOptional(String id) {
        return Optional.ofNullable(MESSAGE_TYPE_MAP.get(id));
    }

    public String getId() {
        return id;
    }

    public Class<? extends MessageData> getMessageDataClass() {
        return messageDataClass;
    }
}
