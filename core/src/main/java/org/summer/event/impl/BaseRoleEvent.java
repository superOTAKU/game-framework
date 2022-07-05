package org.summer.event.impl;

import lombok.Data;
import org.summer.event.Event;

@Data
public class BaseRoleEvent implements Event {
    private Long id;
}
