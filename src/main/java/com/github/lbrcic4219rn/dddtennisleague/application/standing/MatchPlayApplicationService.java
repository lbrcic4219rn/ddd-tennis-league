package com.github.lbrcic4219rn.dddtennisleague.application.standing;

import com.github.lbrcic4219rn.dddtennisleague.application.standing.dto.MatchDto;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.GroupId;
import com.github.lbrcic4219rn.dddtennisleague.domain.player.PlayerId;
import com.github.lbrcic4219rn.dddtennisleague.domain.standing.Match;
import com.github.lbrcic4219rn.dddtennisleague.domain.standing.MatchId;
import com.github.lbrcic4219rn.dddtennisleague.domain.standing.MatchRepo;
import com.github.lbrcic4219rn.dddtennisleague.domain.standing.MatchResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MatchPlayApplicationService {
    private final MatchRepo matchRepo;
    private final StandingsApplicationService standingsService;

    public MatchPlayApplicationService(MatchRepo matchRepo, StandingsApplicationService standingsService) {
        this.matchRepo = matchRepo;
        this.standingsService = standingsService;
    }

    public String createMatch(String groupId, String homePlayerId, String awayPlayerId) {
        // Invariant: Both players must be in the same group (validated by service caller)
        GroupId groupIdObj = new GroupId(UUID.fromString(groupId));
        PlayerId homePlayerIdObj = new PlayerId(UUID.fromString(homePlayerId));
        PlayerId awayPlayerIdObj = new PlayerId(UUID.fromString(awayPlayerId));

        if (homePlayerIdObj.equals(awayPlayerIdObj)) {
            throw new IllegalArgumentException("Home and away player must be different");
        }

        Match match = new Match(groupIdObj, homePlayerIdObj, awayPlayerIdObj);
        matchRepo.save(match);

        return match.getId().value().toString();
    }

    public void completeMatch(String matchId, String winnerId, String loserId, boolean walkover) {
        MatchId matchIdObj = new MatchId(UUID.fromString(matchId));
        PlayerId winnerIdObj = new PlayerId(UUID.fromString(winnerId));
        PlayerId loserIdObj = new PlayerId(UUID.fromString(loserId));

        Optional<Match> match = matchRepo.findById(matchIdObj);
        if (match.isEmpty()) {
            throw new IllegalArgumentException("Match not found: " + matchId);
        }

        Match matchObj = match.get();
        MatchResult result = new MatchResult(winnerIdObj, loserIdObj, walkover);
        matchObj.setResult(result);
        matchRepo.save(matchObj);

        // Trigger leaderboard update (invariant: MatchCompleted event recalculates standings)
        standingsService.updateStandingsForMatch(matchObj);
    }

    public MatchDto getMatchById(String matchId) {
        MatchId matchIdObj = new MatchId(UUID.fromString(matchId));
        Optional<Match> match = matchRepo.findById(matchIdObj);
        return match.map(this::convertToDto).orElse(null);
    }

    public List<MatchDto> getMatchesByGroup(String groupId) {
        GroupId groupIdObj = new GroupId(UUID.fromString(groupId));
        return matchRepo.findByGroupId(groupIdObj)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<MatchDto> getAllMatches() {
        return matchRepo.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public void removeMatch(String matchId) {
        MatchId matchIdObj = new MatchId(UUID.fromString(matchId));
        matchRepo.remove(matchIdObj);
    }

    private MatchDto convertToDto(Match match) {
        String winnerId = match.getResult() != null ? match.getResult().winner().value().toString() : null;
        String loserId = match.getResult() != null ? match.getResult().loser().value().toString() : null;

        return new MatchDto(
                match.getId().value().toString(),
                match.getGroupId().value().toString(),
                match.getHomePlayer().value().toString(),
                match.getAwayPlayer().value().toString(),
                winnerId,
                loserId,
                match.getResult() != null ? match.getResult().walkover() : false
        );
    }
}

