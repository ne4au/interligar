package com.ne4ay.interligar.capture;

import com.ne4ay.interligar.Executable;
import javafx.stage.Screen;

import java.awt.MouseInfo;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScreenCapturer implements AutoCloseable, Runnable, Executable {

    private final ExecutorService service = Executors.newSingleThreadExecutor(runnable -> {
        Thread t = Executors.defaultThreadFactory().newThread(runnable);
        t.setDaemon(true);
        return t;
    });
    private volatile CompletableFuture<Void> future;
    private volatile boolean hasToRun = true;
    private volatile boolean isRunning = false;

    public ScreenCapturer() {
        System.out.println(Screen.getScreens()); //TODO: clean

    }

    public void start() throws InterruptedException {
        if (this.isRunning) {
            stop();
        }
        this.hasToRun = true;
        this.future = CompletableFuture.runAsync(this, this.service);
    }

    @Override
    public void stop() throws InterruptedException {
        this.hasToRun = false;
        this.future.join();
    }
    
    @Override
    public boolean isRunning() {
        return this.isRunning;
    }


    @Override
    public void run() {
        this.isRunning = true;
        while (this.hasToRun) {
//            System.out.println(MouseInfo.getPointerInfo().getLocation()); //TODO: clean
            try {
                Thread.currentThread().join(5);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        this.isRunning = false;
    }

    @Override
    public void close() throws Exception {
        this.service.shutdown();
    }

}
