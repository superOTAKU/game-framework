package org.summer.event.impl;

import lombok.Data;
import org.summer.event.EventBus;

@Data
public class RoleResource {
    private EventBus eventBus;
    private Role role;

    //将操作封装成为一个event
    public void changeName(String name) {
        RoleNameChangeEvent event = new RoleNameChangeEvent();
        event.setId(role.getId());
        event.setName(name);
        eventBus.putEvent(event);
    }

    public void addExp(Long exp) {
        RoleExpEvent expEvent = new RoleExpEvent();
        expEvent.setId(role.getId());
        expEvent.setExp(exp);
        eventBus.putEvent(expEvent);
    }

}
