package com.github.lbrcic4219rn.dddtennisleague.application.standing;

import com.github.lbrcic4219rn.dddtennisleague.application.standing.dto.LeaderboardDto;
import com.github.lbrcic4219rn.dddtennisleague.application.standing.dto.StandingEntryDto;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.GroupId;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.MembershipRepo;
import com.github.lbrcic4219rn.dddtennisleague.domain.player.PlayerId;
import com.github.lbrcic4219rn.dddtennisleague.domain.standing.Leaderboard;
import com.github.lbrcic4219rn.dddtennisleague.domain.standing.LeaderboardId;
import com.github.lbrcic4219rn.dddtennisleague.domain.standing.LeaderboardRepo;
import com.github.lbrcic4219rn.dddtennisleague.domain.standing.Match;
import com.github.lbrcic4219rn.dddtennisleague.domain.standing.MatchRepo;
import com.github.lbrcic4219rn.dddtennisleague.domain.standing.StandingEntry;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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

        // Invariant: One leaderboard per group (scoped per group)
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
        // Get all members of the group
        List<PlayerId> memberIds = membershipRepo.findByGroupId(groupId)
                .stream()
                .map(m -> m.getPlayerId())
                .collect(Collectors.toList());

        // Get all completed matches for this group
        List<Match> groupMatches = matchRepo.findByGroupId(groupId)
                .stream()
                .filter(m -> m.getResult() != null)
                .collect(Collectors.toList());

        // Calculate standings for each member
        Map<PlayerId, StandingStats> stats = new HashMap<>();
        for (PlayerId memberId : memberIds) {
            stats.put(memberId, new StandingStats(memberId));
        }

        // Process each match
        for (Match match : groupMatches) {
            PlayerId winner = match.getResult().winner();
            PlayerId loser = match.getResult().loser();

            if (stats.containsKey(winner)) {
                stats.get(winner).addWin();
            }
            if (stats.containsKey(loser)) {
                stats.get(loser).addLoss();
            }

            // Sets calculation (assuming 1 set won for winner, 1 for loser)
            if (stats.containsKey(winner)) {
                stats.get(winner).setsWon++;
            }
            if (stats.containsKey(loser)) {
                stats.get(loser).setsLost++;
            }
        }

        // Convert to StandingEntry and sort by rank
        List<StandingStats> sortedStats = stats.values()
                .stream()
                .sorted((a, b) -> {
                    // Sort by points descending, then by wins descending
                    if (b.points != a.points) {
                        return Integer.compare(b.points, a.points);
                    }
                    return Integer.compare(b.wins, a.wins);
                })
                .collect(Collectors.toList());

        List<StandingEntry> entries = new ArrayList<>();
        for (int i = 0; i < sortedStats.size(); i++) {
            StandingStats stat = sortedStats.get(i);
            entries.add(new StandingEntry(
                    stat.playerId,
                    i + 1,
                    stat.points,
                    stat.wins,
                    stat.losses,
                    stat.setsWon,
                    stat.setsLost
            ));
        }

        return entries;
    }

    private LeaderboardDto convertToDto(Leaderboard leaderboard) {
        List<StandingEntryDto> entryDtos = leaderboard.getEntries()
                .stream()
                .map(entry -> new StandingEntryDto(
                        entry.playerId().value().toString(),
                        entry.rank(),
                        entry.points(),
                        entry.wins(),
                        entry.losses(),
                        entry.setsWon(),
                        entry.setsLost()
                ))
                .collect(Collectors.toList());

        return new LeaderboardDto(
                leaderboard.getId().value().toString(),
                leaderboard.getGroupId().value().toString(),
                leaderboard.getLastUpdated(),
                entryDtos
        );
    }

    private static class StandingStats {
        PlayerId playerId;
        int wins = 0;
        int losses = 0;
        int setsWon = 0;
        int setsLost = 0;

        StandingStats(PlayerId playerId) {
            this.playerId = playerId;
        }

        void addWin() {
            this.wins++;
            this.points += 3;
        }

        void addLoss() {
            this.losses++;
            this.points += 1;
        }

        int points = 0;
    }
}


