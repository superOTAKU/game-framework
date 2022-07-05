package org.summer.event.impl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RoleManager {
    private final ConcurrentMap<Long, RoleResource> roleIdMap = new ConcurrentHashMap<>();
    private static final RoleManager INSTANCE = new RoleManager();

    public static RoleManager getInstance() {
        return INSTANCE;
    }

    public RoleResource getRoleResource(Long id) {
        return roleIdMap.get(id);
    }

    public void addRoleResource(Long id, RoleResource roleResource) {
        roleIdMap.put(id, roleResource);
    }

}
