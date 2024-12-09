package com.ne4ay.interligar.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MousePosition(@JsonProperty int deltaX, @JsonProperty int deltaY) {
}
