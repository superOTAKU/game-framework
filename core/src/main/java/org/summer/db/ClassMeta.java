package org.summer.db;

import lombok.Data;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 根据实体class，得到的元数据
 */
@Data
public class ClassMeta {
    private String name;
    private List<String> fieldNames;
    private List<Method> setters;
    private String querySql;
}
