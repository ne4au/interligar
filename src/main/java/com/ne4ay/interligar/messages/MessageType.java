package com.ne4ay.interligar.messages;

import com.ne4ay.interligar.messages.data.ChangeDestinationModeRequestMessageData;
import com.ne4ay.interligar.messages.data.ChangeDestinationModeResponseMessageData;
import com.ne4ay.interligar.messages.data.MouseChangePositionMessageData;
import com.ne4ay.interligar.messages.data.TestMessageData;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class MessageType<T extends MessageData> {
    public static final MessageType<TestMessageData> TEST =
        new MessageType<>("test", TestMessageData.class);
    public static final MessageType<ChangeDestinationModeRequestMessageData> CHANGE_DESTINATION_REQUEST =
        new MessageType<>("ch_d_req", ChangeDestinationModeRequestMessageData.class);
    public static final MessageType<ChangeDestinationModeResponseMessageData> CHANGE_DESTINATION_RESPONSE =
        new MessageType<>("ch_d_resp", ChangeDestinationModeResponseMessageData.class);
    public static final MessageType<MouseChangePositionMessageData> MOUSE_CHANGE_POSITION =
        new MessageType<>("m_ch_p", MouseChangePositionMessageData.class);
    ;
    private static final Map<String, MessageType<?>> MESSAGE_TYPE_MAP = values()
        .stream()
        .collect(Collectors.toMap(
            MessageType::getId,
            Function.identity()
        ));

    private final String id;
    private final Class<T> messageDataClass;

    MessageType(String id, Class<T> messageDataClass) {
        this.id = requireNonNull(id, "id");
        this.messageDataClass = requireNonNull(messageDataClass, "messageDataClass");
    }

    private static Set<MessageType<?>> values() {
        return Set.of(TEST, CHANGE_DESTINATION_REQUEST, CHANGE_DESTINATION_RESPONSE);
    }

    public static MessageType<?> getMessageTypeForId(String id) {
        return MESSAGE_TYPE_MAP.get(id);
    }

    public static Optional<MessageType<?>> getMessageTypeForIdOptional(String id) {
        return Optional.ofNullable(MESSAGE_TYPE_MAP.get(id));
    }

    public String getId() {
        return id;
    }

    public Class<T> getMessageDataClass() {
        return messageDataClass;
    }
}
