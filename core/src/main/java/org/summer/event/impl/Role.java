package org.summer.event.impl;

import lombok.Data;

@Data
public class Role {
    private Long id;
    private String name;
    private Integer level;
    private Long exp;
}
