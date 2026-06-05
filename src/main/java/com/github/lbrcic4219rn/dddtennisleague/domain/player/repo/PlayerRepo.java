package com.github.lbrcic4219rn.dddtennisleague.domain.player.repo;

import com.github.lbrcic4219rn.dddtennisleague.domain.player.Player;
import com.github.lbrcic4219rn.dddtennisleague.domain.player.id.PlayerId;

import java.util.Map;
import java.util.Optional;

public interface PlayerRepo {
    void save(Player player);
    Optional<Player> findById(PlayerId playerId);
    void remove(PlayerId playerId);
    Map<PlayerId, Player> findAll();
}
