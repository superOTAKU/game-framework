package org.summer.game.player;

import lombok.Data;

/**
 * player瞬时视图
 */
@Data
public class PlayerView {
    private Long playerId;
    private Long teamId;
    private Long sceneId;
}
