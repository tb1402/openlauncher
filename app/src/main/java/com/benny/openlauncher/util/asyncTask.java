package com.benny.openlauncher.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This abstract class is the implementation of a basic asynchronous task, based on java.util.concurrent, fitted for the needs in this project.
 * Class is necessary because Android's asyncTask class is deprecated since API level 30
 */
public abstract class asyncTask implements Runnable {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();//executor service, with one workerThread

    /**
     * Method to pass the runnable (this) to the executorService and thus execute it
     */
    public void execute() {
        executorService.execute(this);
    }

    /**
     * run method of runnable
     */
    @Override
    public void run() {
        doWork();
        executorService.shutdown();
    }

    /**
     * Abstract method which is called after starting the task, this method runs on another thread and can do work in background
     */
    protected abstract void doWork();
}