package org.summer.game.team;

import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.EventExecutor;

/**
 * 组队系统
 */
public class TeamManager {
    //组队逻辑业务线程
    private final EventExecutor eventExecutor = new DefaultEventExecutor();
}
