package org.summer.db;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class GenericEntity {
    //表名
    private String name;
    //id字段值
    private Long idValue;
    //每个字段以及字段值
    private Map<String, Object> fieldValues = new LinkedHashMap<>();
    //实体状态
    private EntityState state;

}
