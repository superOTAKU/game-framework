package org.summer.event;

import org.summer.thread.OrderQueuedThreadExecutor;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * 主要是将event的投递和event的执行分开
 *
 */
public class EventBus {
    protected final List<EventListener> eventListeners = new CopyOnWriteArrayList<>();
    protected final String key;
    protected final OrderQueuedThreadExecutor queuedExecutor;

    public EventBus(String key, OrderQueuedThreadExecutor queuedExecutor) {
        this.key = key;
        this.queuedExecutor = queuedExecutor;
    }

    public void putEvent(Event event) {
        queuedExecutor.addTask(key, () -> handleEvent(event));
    }

    public void addEventListener(EventListener<? extends Event> eventListener) {
        eventListeners.add(eventListener);
    }

    public void handleEvent(Event event) {
        for (var listener : eventListeners) {
            listener.onEvent(event);
        }
    }

}
