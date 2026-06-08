package com.github.lbrcic4219rn.dddtennisleague.application.league;

import com.github.lbrcic4219rn.dddtennisleague.application.league.dto.GroupDto;
import com.github.lbrcic4219rn.dddtennisleague.application.league.dto.LeagueDto;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.Group;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.id.GroupId;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.League;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.id.LeagueId;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.Season;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.SkillLevel;
import com.github.lbrcic4219rn.dddtennisleague.infrastructure.persistence.GroupRepoInMemory;
import com.github.lbrcic4219rn.dddtennisleague.infrastructure.persistence.LeagueRepoInMemory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LeagueApplicationService {
    private final LeagueRepoInMemory leagueRepo;
    private final GroupRepoInMemory groupRepo;

    public LeagueId createLeague(String name, Instant startDate, Instant endDate) {
        Season season = new Season(startDate, endDate);
        League league = new League(name, season);
        leagueRepo.save(league);
        return league.getLeagueId();
    }

    public GroupId createGroup(LeagueId leagueId, SkillLevel skillLevel) {
        Optional<League> league = leagueRepo.findById(leagueId);
        if (league.isEmpty()) {
            throw new IllegalArgumentException("League not found: " + leagueId);
        }

        GroupId groupId = new GroupId(UUID.randomUUID());
        Group group = new Group(groupId, leagueId, skillLevel);
        groupRepo.save(group);
        league.get().addGroup(group);
        leagueRepo.save(league.get());

        return groupId;
    }

    public LeagueDto getLeagueById(LeagueId leagueId) throws IllegalArgumentException {
        League league = leagueRepo.findById(leagueId)
                .orElseThrow(IllegalArgumentException::new);
        return getLeagueDto(league);
    }

    public GroupDto getGroupById(GroupId groupId) {
        Group group = groupRepo.findById(groupId).orElseThrow(IllegalArgumentException::new);
        return getGroupDto(group);
    }

    public List<LeagueDto> getAllLeagues() {
        return leagueRepo.findAll()
                .values()
                .stream()
                .map(this::getLeagueDto)
                .toList();
    }

    public void removeLeague(LeagueId leagueId) throws IllegalArgumentException {
        leagueRepo.findById(leagueId).orElseThrow(IllegalArgumentException::new);
        groupRepo.removeGroupsByLeagueId(leagueId);
        leagueRepo.remove(leagueId);
    }

    public void removeGroup(GroupId groupId) throws IllegalArgumentException {
        groupRepo.findById(groupId).orElseThrow(IllegalArgumentException::new);
        leagueRepo.removeGroup(groupId);
        groupRepo.remove(groupId);
    }

    private GroupDto getGroupDto(Group group) {
        return new GroupDto(
                group.getId().value().toString(),
                group.getLeagueId().value().toString(),
                group.getLevel().toString(),
                group.getMemberships().size()
        );
    }

    private LeagueDto getLeagueDto(League league) {
        return new LeagueDto(
                league.getLeagueId().value().toString(),
                league.getName(),
                league.getSeason().startDate(),
                league.getSeason().endDate(),
                league.getGroups()
                        .stream()
                        .map(this::getGroupDto)
                        .toList()
        );
    }
}

