package org.summer.event.impl;

import org.summer.event.EventBus;
import org.summer.thread.OrderQueuedThreadExecutor;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class App {

    public static void main(String[] args) throws Exception {
        Role role = new Role();
        role.setId(1L);
        role.setLevel(1);
        role.setExp(0L);
        role.setName("Jack");
        OrderQueuedThreadExecutor executor = new OrderQueuedThreadExecutor(4, 4, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        EventBus eventBus = new EventBus(String.valueOf(role.getId()), executor);
        eventBus.addEventListener(new RoleEventListener());
        RoleResource roleResource = new RoleResource();
        roleResource.setRole(role);
        roleResource.setEventBus(eventBus);
        RoleManager.getInstance().addRoleResource(role.getId(), roleResource);
        roleResource.changeName("Marry");
        roleResource.addExp(100L);
        Thread.sleep(100L);
        System.out.println(role);
    }

}
