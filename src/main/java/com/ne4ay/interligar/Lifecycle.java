package com.ne4ay.interligar;

public interface Lifecycle {

    void start();

    void stop() throws InterruptedException;

    boolean isRunning();
}
