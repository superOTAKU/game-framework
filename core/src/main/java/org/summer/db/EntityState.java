package org.summer.db;

public enum EntityState {
    //等待insert到db
    INSERTING,
    //常驻内存，可执行修改
    MODIFYING,
    //等待从db删除
    DELETING,
    //已删除
    DELETED
}
