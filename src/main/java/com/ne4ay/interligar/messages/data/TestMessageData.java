package com.ne4ay.interligar.messages.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ne4ay.interligar.messages.MessageData;

public record TestMessageData(@JsonProperty String text) implements MessageData {}
