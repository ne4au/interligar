package com.ne4ay.interligar;

public interface Executable {

    void start() throws InterruptedException;

    void stop() throws InterruptedException;

    boolean isRunning();

}
