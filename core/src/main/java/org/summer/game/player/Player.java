package org.summer.game.player;

import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.EventExecutor;

/**
 * 玩家全局数据缓存，跨系统的数据请更新到这里
 */
public class Player {
    //用户线程，处理用户自身数据的更新
    private final EventExecutor eventExecutor;
    //玩家id
    private Long playerId;
    //所在队伍id
    private Long teamId;
    //所在场景id
    private Long sceneId;

    public Player(EventExecutor eventExecutor) {
        this.eventExecutor = eventExecutor;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public Long getTeamId() {
        return teamId;
    }

    public Long getSceneId() {
        return sceneId;
    }

    public void setSceneId(Long sceneId) {
        this.sceneId = sceneId;
    }

    public EventExecutor getEventExecutor() {
        return eventExecutor;
    }

    public PlayerView asView() {
        PlayerView view = new PlayerView();
        view.setPlayerId(playerId);
        view.setSceneId(sceneId);
        view.setTeamId(teamId);
        return view;
    }

    //TODO 每当玩家数据发送变更，要通过Listener的方式进行通知，例如某个Listener会将最新的PlayerView传到Scene中

    public static void main(String[] args) {
        DefaultPromise<PlayerView> playerPromise = new DefaultPromise<>(new DefaultEventExecutor());
        playerPromise.addListener(f -> {
           if (f.isSuccess()) {
               PlayerView player = (PlayerView) f.get();
               Long teamId = player.getTeamId();
               System.out.println("player is at team " + teamId);
           }
        });
        //其他线程
        PlayerManager.getInstance().getPlayer(1L, playerPromise);
    }
}
