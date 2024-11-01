package com.ne4ay.interligar.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TestMessageData(@JsonProperty String text) implements MessageData {}
