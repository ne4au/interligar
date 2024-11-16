package com.ne4ay.interligar;

import com.ne4ay.interligar.messages.Message;

public interface Channel extends Executable{

    void sendMessage(Message<?> message);
}
