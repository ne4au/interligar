package com.ne4ay.interligar.messages;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;

import java.util.Optional;
import java.util.function.Consumer;

import static com.ne4ay.interligar.messages.MessageType.getMessageTypeForId;

public final class MessagesUtils {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String ID_FIELD = "id";
    private static final String DATA_FIELD = "data";
    private MessagesUtils() {}

    public static Optional<Message<?>> parse(String message, Consumer<Exception> exceptionHandler) {
        try {
            JsonNode root = OBJECT_MAPPER.readTree(message);
            JsonNode idField = root.get(ID_FIELD);
            if (idField == null) {
                throw new IllegalStateException("Got corrupted message without id field: " + message);
            }
            String id = idField.asText();
            MessageType messageType = getMessageTypeForId(id);
            if (messageType == null) {
                throw new IllegalStateException("Unknown message type got: `" + id + "`");
            }
            JsonNode dataNode = root.get(DATA_FIELD);
            if (dataNode == null) {
                throw new IllegalStateException("Got corrupted message without data field");
            }
            MessageData messageData = OBJECT_MAPPER.readValue(new TreeTraversingParser(dataNode), messageType.getMessageDataClass());
            return Optional.of(new Message<>(messageType, messageData));
        } catch (Exception e) {
            exceptionHandler.accept(e);
            return Optional.empty();
        }
    }

    public static Optional<String> serialize(Message<?> message, Consumer<Exception> exceptionHandler) {
        try {
            return Optional.of(OBJECT_MAPPER.writeValueAsString(MessageModel.fromMessage(message)));
        } catch (Exception e) {
            exceptionHandler.accept(e);
            return Optional.empty();
        }
    }


}
