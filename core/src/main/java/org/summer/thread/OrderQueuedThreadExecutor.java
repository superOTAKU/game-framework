package org.summer.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 
 */
public class OrderQueuedThreadExecutor extends ThreadPoolExecutor {
    private final ConcurrentMap<String, Worker> taskQueueMap = new ConcurrentHashMap<>();

    public OrderQueuedThreadExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public OrderQueuedThreadExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public OrderQueuedThreadExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public OrderQueuedThreadExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    public void addTask(String key, Runnable task) {
        Worker worker = taskQueueMap.computeIfAbsent(key, k -> new Worker());
        synchronized (worker) {
            worker.queue.add(task);
            if (!worker.processing) {
                worker.processing = true;
                execute(worker);
            }
        }
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        if (r instanceof Worker) {
            Worker worker = (Worker)r;
            synchronized (worker) {
                if (!worker.processing && worker.queue.size() > 0) {
                    worker.processing = true;
                    execute(worker);
                }
            }
        }
    }

    static class Worker implements Runnable {
        List<Runnable> queue = new ArrayList<>();
        boolean processing = false;

        @Override
        public void run() {
            Runnable task;
            synchronized (this) {
                task = queue.remove(0);
            }
            try {
                if (task != null) {
                    task.run();
                }
            } finally {
                processing = false;
            }
        }
    }

    public static void main(String[] args) {
        OrderQueuedThreadExecutor queuedExecutor = new OrderQueuedThreadExecutor(4, 4, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        for (int i = 0; i < 1000; i++) {
            final int idx = i;
            queuedExecutor.addTask("1", () -> System.out.println(idx));
        }
    }


}
