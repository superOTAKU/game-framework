package org.summer.game.map;

import io.netty.util.concurrent.EventExecutor;
import org.summer.game.player.PlayerManager;
import org.summer.game.player.PlayerView;

import java.util.List;
import java.util.Map;

//保证场景自己的内容一定在自己的线程操作，主要维护
public class Scene {
    //单个场景自身线程
    private EventExecutor eventExecutor;
    private Long id;
    //场景中要维护玩家的形象等信息，复制一份出来在场景线程中独立维护
    private Map<Long, PlayerView> playerViews;
    //如果只有一个层级，可能导致同屏人数过多，所以这里分层级，保证每个层的人数合理
    private List<Layer> layers;

    public EventExecutor getEventExecutor() {
        return eventExecutor;
    }

    public void init() {
        //需要定时同步场景中的玩家数据给到客户端...
        //eventExecutor.scheduleAtFixedRate();
    }

    public void addPlayer(Long playerId) {
        if (playerViews.containsKey(playerId)) {
            return;
        }

        PlayerManager.getInstance().getPlayer(playerId, eventExecutor.<PlayerView>newPromise().addListener(f -> {
            PlayerView view = (PlayerView)f.get();
            playerViews.put(playerId, view);
            //...
            PlayerManager.getInstance().runInPlayerThread(playerId, player -> {
                player.setSceneId(id);
            });
        }));
    }

    public static class Layer {
        //场景中的所有格子
        Cell[][] cells;
    }

    public static class Cell {
        //格子中每个玩家的具体位置
        Map<Long, Position> positions;
    }

    public static class Position {
        int x;
        int y;
    }
}
