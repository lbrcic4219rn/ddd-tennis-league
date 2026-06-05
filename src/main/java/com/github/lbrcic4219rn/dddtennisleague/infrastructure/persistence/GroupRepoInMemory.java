package com.github.lbrcic4219rn.dddtennisleague.infrastructure.persistence;

import com.github.lbrcic4219rn.dddtennisleague.domain.league.Group;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.id.GroupId;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.repo.GroupRepo;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.id.LeagueId;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class GroupRepoInMemory implements GroupRepo {
    private final Map<GroupId, Group> groups = new HashMap<>();

    @Override
    public void save(Group group) {
        groups.put(group.getId(), group);
    }

    @Override
    public Optional<Group> findById(GroupId groupId) {
        return Optional.ofNullable(groups.get(groupId));
    }

    @Override
    public void remove(GroupId groupId) {
        groups.remove(groupId);
    }

    @Override
    public List<Group> findByLeagueId(LeagueId leagueId) {
        return groups.values()
                .stream()
                .filter(group -> group.getLeagueId().equals(leagueId))
                .collect(Collectors.toList());
    }

    public Map<GroupId, Group> findAll() {
        return new HashMap<>(groups);
    }
}

