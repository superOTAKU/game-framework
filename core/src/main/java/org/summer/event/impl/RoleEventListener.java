package org.summer.event.impl;

import org.summer.event.EventListener;

public class RoleEventListener implements EventListener<BaseRoleEvent> {

    @Override
    public void onEvent(BaseRoleEvent event) {
        RoleResource role = RoleManager.getInstance().getRoleResource(event.getId());
        if (event instanceof RoleNameChangeEvent) {
            role.getRole().setName(((RoleNameChangeEvent) event).getName());
        } else if (event instanceof RoleExpEvent) {
            role.getRole().setExp(role.getRole().getExp() + ((RoleExpEvent) event).getExp());
        }
    }

}
