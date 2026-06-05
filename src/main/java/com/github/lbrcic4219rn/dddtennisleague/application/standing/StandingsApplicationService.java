package com.github.lbrcic4219rn.dddtennisleague.application.standing;

import com.github.lbrcic4219rn.dddtennisleague.application.standing.dto.LeaderboardDto;
import com.github.lbrcic4219rn.dddtennisleague.application.standing.dto.StandingEntryDto;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.Membership;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.id.GroupId;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.repo.MembershipRepo;
import com.github.lbrcic4219rn.dddtennisleague.domain.player.id.PlayerId;
import com.github.lbrcic4219rn.dddtennisleague.domain.standing.Leaderboard;
import com.github.lbrcic4219rn.dddtennisleague.domain.standing.id.LeaderboardId;
import com.github.lbrcic4219rn.dddtennisleague.domain.standing.repo.LeaderboardRepo;
import com.github.lbrcic4219rn.dddtennisleague.domain.standing.Match;
import com.github.lbrcic4219rn.dddtennisleague.domain.standing.repo.MatchRepo;
import com.github.lbrcic4219rn.dddtennisleague.domain.standing.StandingEntry;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class StandingsApplicationService {
    private final LeaderboardRepo leaderboardRepo;
    private final MatchRepo matchRepo;
    private final MembershipRepo membershipRepo;

    public StandingsApplicationService(LeaderboardRepo leaderboardRepo, MatchRepo matchRepo, MembershipRepo membershipRepo) {
        this.leaderboardRepo = leaderboardRepo;
        this.matchRepo = matchRepo;
        this.membershipRepo = membershipRepo;
    }

    public String createLeaderboard(String groupId) {
        GroupId groupIdObj = new GroupId(UUID.fromString(groupId));

        Optional<Leaderboard> existing = leaderboardRepo.findByGroupId(groupIdObj);
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Leaderboard already exists for group: " + groupId);
        }

        Leaderboard leaderboard = new Leaderboard(groupIdObj);
        leaderboardRepo.save(leaderboard);

        return leaderboard.getId().value().toString();
    }

    public void updateStandingsForMatch(Match match) {
        GroupId groupId = match.getGroupId();

        Optional<Leaderboard> leaderboardOpt = leaderboardRepo.findByGroupId(groupId);
        if (leaderboardOpt.isEmpty()) {
            // Create leaderboard if it doesn't exist
            Leaderboard leaderboard = new Leaderboard(groupId);
            leaderboardRepo.save(leaderboard);
            leaderboardOpt = Optional.of(leaderboard);
        }

        Leaderboard leaderboard = leaderboardOpt.get();

        // Recalculate standings (invariant: MatchCompleted event recalculates standings)
        List<StandingEntry> updatedEntries = recalculateStandings(groupId);
        leaderboard.setEntries(updatedEntries);
        leaderboardRepo.save(leaderboard);
    }

    public LeaderboardDto getLeaderboard(String leaderboardId) {
        LeaderboardId leaderboardIdObj = new LeaderboardId(UUID.fromString(leaderboardId));
        Optional<Leaderboard> leaderboard = leaderboardRepo.findById(leaderboardIdObj);
        return leaderboard.map(this::convertToDto).orElse(null);
    }

    public LeaderboardDto getLeaderboardByGroup(String groupId) {
        GroupId groupIdObj = new GroupId(UUID.fromString(groupId));
        Optional<Leaderboard> leaderboard = leaderboardRepo.findByGroupId(groupIdObj);
        return leaderboard.map(this::convertToDto).orElse(null);
    }

    public List<LeaderboardDto> getAllLeaderboards() {
        return leaderboardRepo.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public void removeLeaderboard(String leaderboardId) {
        LeaderboardId leaderboardIdObj = new LeaderboardId(UUID.fromString(leaderboardId));
        leaderboardRepo.remove(leaderboardIdObj);
    }

    private List<StandingEntry> recalculateStandings(GroupId groupId) {
        return null;
    }

    private LeaderboardDto convertToDto(Leaderboard leaderboard) {
        List<StandingEntry> entries = leaderboard.getEntries();
        List<StandingEntryDto> entryDtos = IntStream.range(0, entries.size())
                .mapToObj(i -> {
                    StandingEntry entry = entries.get(i);
                    return new StandingEntryDto(
                            entry.playerId().value().toString(),
                            i + 1,
                            entry.points(),
                            entry.wins(),
                            entry.losses(),
                            entry.setsWon(),
                            entry.setsLost()
                    );
                }).toList();

        return new LeaderboardDto(
                leaderboard.getId().value().toString(),
                leaderboard.getGroupId().value().toString(),
                leaderboard.getLastUpdated(),
                entryDtos
        );
    }
}


