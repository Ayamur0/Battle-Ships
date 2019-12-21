package com.battleships.gui.toolbox;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParallelTasks {

    private static final Collection<Runnable> tasks = new ArrayList<Runnable>();

    public static void add(final Runnable task)
    {
        tasks.add(task);
    }

    public static void go() throws InterruptedException
    {
        final ExecutorService threads = Executors.newFixedThreadPool(Runtime.getRuntime()
                .availableProcessors());
        try
        {
            final CountDownLatch latch = new CountDownLatch(tasks.size());
            for (final Runnable task : tasks)
                threads.execute(task);
            latch.await();
        }
        finally
        {
            threads.shutdown();
        }
    }
}

