package org.summer.game.map;

import io.netty.util.concurrent.EventExecutor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SceneManager {
    //管理所有场景的线程
    private EventExecutor eventExecutor;
    //当前所有的场景
    private final Map<Long, Scene> sceneMap = new ConcurrentHashMap<>();

    public void addPlayerToScene(Long playerId, Long sceneId) {
        eventExecutor.submit(() -> {
           sceneMap.get(sceneId).getEventExecutor().submit(() -> {
              sceneMap.get(sceneId).addPlayer(playerId);
           });
        });
    }
}
