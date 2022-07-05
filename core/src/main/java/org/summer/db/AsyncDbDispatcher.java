package org.summer.db;

/**
 * 分发异步db操作，抽象出来以先完成SqlExecutor部分
 */
public interface AsyncDbDispatcher {

    void dispatch(GenericEntity entity);

}
