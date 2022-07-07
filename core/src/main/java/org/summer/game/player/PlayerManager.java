package org.summer.game.player;

import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Promise;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

public class PlayerManager {
    private final ConcurrentMap<Long, Player> playerMap = new ConcurrentHashMap<>();

    private static final PlayerManager INSTANCE = new PlayerManager();

    public static PlayerManager getInstance() {
        return INSTANCE;
    }

    public void runInPlayerThread(Long playerId, Consumer<Player> consumer) {
        playerMap.get(playerId).getEventExecutor().submit(() -> {
            consumer.accept(playerMap.get(playerId));
        });
    }

    public void runInPlayerThread(Long playerId, Consumer<Player> consumer, Promise<PlayerView> promise) {
        playerMap.get(playerId).getEventExecutor().submit(() -> {
            promise.setSuccess(playerMap.get(playerId).asView());
            consumer.accept(playerMap.get(playerId));
        });
    }

    public Player getPlayer(Long playerId) {
        return playerMap.get(playerId);
    }

    public void getPlayer(Long playerId, Promise<PlayerView> promise) {
        playerMap.get(playerId).getEventExecutor().execute(() -> {
            Player player = playerMap.get(playerId);
            promise.setSuccess(player.asView());
        });
    }

    public EventExecutor getPlayerExecutor(Long player) {
        return playerMap.get(player).getEventExecutor();
    }
}
