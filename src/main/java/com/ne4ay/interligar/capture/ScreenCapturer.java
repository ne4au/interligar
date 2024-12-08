package com.ne4ay.interligar.capture;

import com.ne4ay.interligar.Executable;
import com.ne4ay.interligar.FunctionUtils;
import com.ne4ay.interligar.data.MouseLocationDelta;
import com.ne4ay.interligar.messages.data.MouseChangePositionMessageData;
import javafx.stage.Screen;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static com.ne4ay.interligar.FunctionUtils.EMPTY_RUNNABLE;
import static com.ne4ay.interligar.FunctionUtils.emptyConsumer;
import static com.ne4ay.interligar.utils.InterligarUtils.getMouseLocation;

public class ScreenCapturer implements AutoCloseable, Runnable, Executable {

    private final ExecutorService service = Executors.newSingleThreadExecutor(runnable -> {
        Thread t = Executors.defaultThreadFactory().newThread(runnable);
        t.setDaemon(true);
        return t;
    });

    private final Object boundsLock = new Object();

    private final Robot robot;

    private volatile CaptureMode captureType;
    private volatile Runnable onGoOutOfBounds = EMPTY_RUNNABLE;
    private volatile Consumer<MouseLocationDelta> onMouseMove = FunctionUtils::emptyConsumer;
    private volatile CompletableFuture<Void> future;
    private volatile boolean hasToRun = true;
    private volatile boolean isRunning = false;
    private volatile double currentX = 0;
    private volatile double currentY = 0;
    private volatile double minX = 0;
    private volatile double minY = 0;
    private volatile double maxX = 0;
    private volatile double maxY = 0;

    private volatile Point previousPoint;
    private volatile AtomicInteger wantsToGoOutOfBoundsCounter = new AtomicInteger(0);

    public ScreenCapturer() throws AWTException {
        this.robot = new Robot();
    }

    public ScreenCapturer setOnGoOutOfBounds(Runnable runnable) {
        this.onGoOutOfBounds = runnable;
        return this;
    }

    public ScreenCapturer setOnMouseMove(Consumer<MouseLocationDelta> onMouseMove) {
        this.onMouseMove = onMouseMove;
        return this;
    }

    public ScreenCapturer setCaptureMode(CaptureMode captureType) {
        this.captureType = captureType;
        return this;
    }

    public ScreenCapturer moveMouse(MouseLocationDelta delta) {
        Point point = getMouseLocation();
        robot.mouseMove(
            (int)point.getX() + (int)delta.delX(),
            (int)point.getY() + (int)delta.delY());
        return this;
    }

    public void start() throws InterruptedException {
        if (this.isRunning) {
            stop();
        }
        this.hasToRun = true;
        setCurrentBounds();
        this.future = CompletableFuture.runAsync(this, this.service);
    }

    private void setCurrentBounds() {
        synchronized (this.boundsLock) {
            Screen primaryScreen = Screen.getScreens().getFirst();
            var rectangleBounds = primaryScreen.getBounds();
            this.minX = rectangleBounds.getMinX();
            this.minY = rectangleBounds.getMinY();
            this.maxX = rectangleBounds.getMaxX();
            this.maxY = rectangleBounds.getMaxY();
        }
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
        this.previousPoint = getMouseLocation();
        while (this.hasToRun) {
            switch (this.captureType) {
                case DESTINATION -> handleDestinationMode();
                case SOURCE -> handleSourceDestinationMode();
            }
            try {
                Thread.currentThread().join(5);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        this.isRunning = false;
    }

    private void handleSourceDestinationMode() {
        Point point = getMouseLocation();
        if (point.equals(this.previousPoint)) {
            return;
        }
        MouseLocationDelta locationDelta = MouseLocationDelta.delta(this.previousPoint, point);
        this.previousPoint = point;
        this.onMouseMove.accept(locationDelta);
    }

    private void handleDestinationMode() {
        Point point = getMouseLocation();
        boolean isChanged = false;
        var x = point.getX();
        var y = point.getY();
        if (x != this.currentX) {
            this.currentX = x;
            isChanged = true;
        }
        if (y != this.currentY) {
            this.currentY = y;
            isChanged = true;
        }
        if (isChanged) {
            System.out.println(point); //TODO: clean
            if (x > this.maxX - 100) {
                if (this.wantsToGoOutOfBoundsCounter.get() >= 3) {
                    this.onGoOutOfBounds.run();
                } else {
                    this.robot.mouseMove((int) this.currentX - 1, (int) this.currentY);
                    this.wantsToGoOutOfBoundsCounter.incrementAndGet();
                }
            } else {
                this.wantsToGoOutOfBoundsCounter.set(0);
            }
        }
    }

    @Override
    public void close() throws Exception {
        this.service.shutdown();
    }

}
