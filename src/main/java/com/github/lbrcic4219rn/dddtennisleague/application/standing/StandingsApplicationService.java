package com.github.lbrcic4219rn.dddtennisleague.application.standing;

import com.github.lbrcic4219rn.dddtennisleague.application.standing.dto.LeaderboardDto;
import com.github.lbrcic4219rn.dddtennisleague.application.standing.dto.StandingEntryDto;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.id.GroupId;
import com.github.lbrcic4219rn.dddtennisleague.domain.player.id.PlayerId;
import com.github.lbrcic4219rn.dddtennisleague.domain.standing.Leaderboard;
import com.github.lbrcic4219rn.dddtennisleague.domain.standing.id.LeaderboardId;
import com.github.lbrcic4219rn.dddtennisleague.domain.standing.repo.LeaderboardRepo;
import com.github.lbrcic4219rn.dddtennisleague.domain.standing.Match;
import com.github.lbrcic4219rn.dddtennisleague.domain.standing.repo.MatchRepo;
import com.github.lbrcic4219rn.dddtennisleague.domain.standing.StandingEntry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class StandingsApplicationService {
    private final LeaderboardRepo leaderboardRepo;
    private final MatchRepo matchRepo;

    public String createLeaderboard(GroupId groupId) throws IllegalArgumentException {
        Optional<Leaderboard> existing = leaderboardRepo.findByGroupId(groupId);
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Leaderboard already exists for group: " + groupId);
        }

        Leaderboard leaderboard = new Leaderboard(groupId);
        leaderboardRepo.save(leaderboard);

        return leaderboard.getId().value().toString();
    }

    public void updateStandingsForMatch(Match match) {
        GroupId groupId = match.getGroupId();

        Optional<Leaderboard> leaderboardOpt = leaderboardRepo.findByGroupId(groupId);
        if (leaderboardOpt.isEmpty()) {
            Leaderboard leaderboard = new Leaderboard(groupId);
            leaderboardRepo.save(leaderboard);
            leaderboardOpt = Optional.of(leaderboard);
        }

        Leaderboard leaderboard = leaderboardOpt.get();
        List<StandingEntry> updatedEntries = recalculateStandings(groupId);
        leaderboard.setEntries(updatedEntries);
        leaderboardRepo.save(leaderboard);
    }

    public LeaderboardDto getLeaderboard(LeaderboardId leaderboardId) {
        Leaderboard leaderboard = leaderboardRepo.findById(leaderboardId)
                .orElseThrow(IllegalArgumentException::new);
        return getLeaderboardDto(leaderboard);
    }

    public LeaderboardDto getLeaderboardByGroup(GroupId groupId) {
        Leaderboard leaderboard = leaderboardRepo.findByGroupId(groupId)
                .orElseThrow(IllegalArgumentException::new);
        return getLeaderboardDto(leaderboard);
    }

    public List<LeaderboardDto> getAllLeaderboards() {
        return leaderboardRepo.findAll()
                .stream()
                .map(this::getLeaderboardDto)
                .toList();
    }

    public void removeLeaderboard(LeaderboardId leaderboardId) {
        leaderboardRepo.remove(leaderboardId);
    }

    private List<StandingEntry> recalculateStandings(GroupId groupId) {
        List<Match> matches = matchRepo.findByGroupId(groupId);
        Map<PlayerId, Double>  points = new HashMap<>();
        Map<PlayerId, Integer> wins = new HashMap<>();
        Map<PlayerId, Integer> losses = new HashMap<>();
        Map<PlayerId, Integer> setsWon = new HashMap<>();

        for (Match match : matches) {
            PlayerId winner = match.getWinner();
            PlayerId loser  = match.getLoser();

            points.putIfAbsent(winner, 0.0);
            points.putIfAbsent(loser, 0.0);
            wins.putIfAbsent(winner, 0);
            wins.putIfAbsent(loser, 0);
            losses.putIfAbsent(winner, 0);
            losses.putIfAbsent(loser, 0);
            setsWon.putIfAbsent(winner, 0);
            setsWon.putIfAbsent(loser, 0);

            points.merge(winner,1.0, Double::sum);
            points.merge(loser,0.5, Double::sum);
            wins.merge(winner,1,Integer::sum);
            losses.merge(loser,1,Integer::sum);

            setsWon.merge(winner, 2, Integer::sum);
            setsWon.merge(loser,  match.getLoserSetsWon(),  Integer::sum);
        }

        Map<PlayerId, Map<PlayerId, Long>> h2h = buildH2HIndex(matches);

        return points.keySet().stream()
                .sorted(Comparator
                        .<PlayerId, Double>comparing(points::get).reversed()
                        .thenComparingInt(setsWon::get).reversed()
                        .thenComparingLong(p -> h2h.getOrDefault(p, Map.of())
                                .values().stream().mapToLong(Long::longValue).sum()).reversed()
                )
                .map(p -> new StandingEntry(
                        p,
                        points.get(p),
                        wins  .get(p),
                        losses.get(p),
                        setsWon.get(p)
                ))
                .toList();
    }

    private Map<PlayerId, Map<PlayerId, Long>> buildH2HIndex(List<Match> matches) {
        Map<PlayerId, Map<PlayerId, Long>> index = new HashMap<>();
        for (Match match : matches) {
            index.computeIfAbsent(match.getWinner(), k -> new HashMap<>())
                    .merge(match.getLoser(), 1L, Long::sum);
        }
        return index;
    }

    private LeaderboardDto getLeaderboardDto(Leaderboard leaderboard) {
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
                            entry.setsWon()
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


