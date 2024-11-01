package com.ne4ay.interligar;

import java.io.Closeable;

public interface Channel extends Runnable, Closeable {

    @Override
    void close();
}
