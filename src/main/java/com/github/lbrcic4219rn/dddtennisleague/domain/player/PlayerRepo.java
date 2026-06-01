package com.github.lbrcic4219rn.dddtennisleague.domain.player;

import java.util.Map;
import java.util.Optional;

public interface PlayerRepo {
    void save(Player player);
    Optional<Player> findById(PlayerId playerId);
    void remove(PlayerId playerId);
    Map<PlayerId, Player> findAll();
}
