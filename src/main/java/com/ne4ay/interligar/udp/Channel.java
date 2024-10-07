package com.ne4ay.interligar.udp;

import java.io.Closeable;
import java.io.IOException;

public interface Channel extends Runnable, Closeable {

    @Override
    void close();
}
