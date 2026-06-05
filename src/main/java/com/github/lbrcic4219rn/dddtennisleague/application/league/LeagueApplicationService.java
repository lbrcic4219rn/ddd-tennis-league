package com.github.lbrcic4219rn.dddtennisleague.application.league;

import com.github.lbrcic4219rn.dddtennisleague.application.league.dto.GroupDto;
import com.github.lbrcic4219rn.dddtennisleague.application.league.dto.LeagueDto;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.Group;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.id.GroupId;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.repo.GroupRepo;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.League;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.id.LeagueId;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.repo.LeagueRepo;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.Season;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.SkillLevel;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LeagueApplicationService {
    private final LeagueRepo leagueRepo;
    private final GroupRepo groupRepo;

    public LeagueApplicationService(LeagueRepo leagueRepo, GroupRepo groupRepo) {
        this.leagueRepo = leagueRepo;
        this.groupRepo = groupRepo;
    }

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

    public LeagueDto getLeagueById(LeagueId leagueId) {
        Optional<League> league = leagueRepo.findById(leagueId);
        if (league.isEmpty()) {
            return null;
        }

        List<GroupDto> groupDtos = league.get()
                .getGroups()
                .stream()
                .map(this::getGroupDto)
                .collect(Collectors.toList());

        return new LeagueDto(
                league.get().getLeagueId().value().toString(),
                league.get().getName(),
                league.get().getSeason().startDate(),
                league.get().getSeason().endDate(),
                groupDtos
        );
    }

    public GroupDto getGroupById(GroupId groupId) {
        Optional<Group> group = groupRepo.findById(groupId);
        return group.map(this::getGroupDto).orElse(null);
    }

    public List<LeagueDto> getAllLeagues() {
        return leagueRepo.findAll()
                .values()
                .stream()
                .map(league -> {
                    List<GroupDto> groupDtos = league.getGroups()
                            .stream()
                            .map(this::getGroupDto)
                            .toList();
                    return new LeagueDto(
                            league.getLeagueId().value().toString(),
                            league.getName(),
                            league.getSeason().startDate(),
                            league.getSeason().endDate(),
                            groupDtos
                    );
                })
                .collect(Collectors.toList());
    }

    public void removeLeague(LeagueId leagueId) {
        leagueRepo.remove(leagueId);
    }

    public void removeGroup(GroupId groupId) {
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
}

