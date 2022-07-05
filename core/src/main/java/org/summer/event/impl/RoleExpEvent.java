package org.summer.event.impl;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RoleExpEvent extends BaseRoleEvent {
    private long exp;
}
