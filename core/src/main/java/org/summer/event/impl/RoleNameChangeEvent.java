package org.summer.event.impl;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RoleNameChangeEvent extends BaseRoleEvent {
    private String name;
}
