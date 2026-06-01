package com.github.lbrcic4219rn.dddtennisleague.infrastructure.persistence;

import com.github.lbrcic4219rn.dddtennisleague.domain.player.Player;
import com.github.lbrcic4219rn.dddtennisleague.domain.player.PlayerId;
import com.github.lbrcic4219rn.dddtennisleague.domain.player.PlayerRepo;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class PlayerRepoInMemory implements PlayerRepo {
    private final Map<PlayerId, Player> players = new HashMap<>();

    public void save(Player player) {
        players.put(player.getId(), player);
    }

    public Optional<Player> findById(PlayerId playerId) {
        return Optional.ofNullable(players.get(playerId));
    }

    public void remove(PlayerId playerId) {
        players.remove(playerId);
    }

    public Map<PlayerId, Player> findAll() {
        return new HashMap<>(players);
    }
}

