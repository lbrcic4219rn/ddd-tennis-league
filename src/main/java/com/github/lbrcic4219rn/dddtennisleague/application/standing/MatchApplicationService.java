package com.github.lbrcic4219rn.dddtennisleague.application.standing;

import com.github.lbrcic4219rn.dddtennisleague.application.standing.dto.MatchDto;
import com.github.lbrcic4219rn.dddtennisleague.application.standing.dto.SetDto;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.id.GroupId;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.repo.MembershipRepo;
import com.github.lbrcic4219rn.dddtennisleague.domain.player.id.PlayerId;
import com.github.lbrcic4219rn.dddtennisleague.domain.standing.Match;
import com.github.lbrcic4219rn.dddtennisleague.domain.standing.Set;
import com.github.lbrcic4219rn.dddtennisleague.domain.standing.TieBreak;
import com.github.lbrcic4219rn.dddtennisleague.domain.standing.id.MatchId;
import com.github.lbrcic4219rn.dddtennisleague.domain.standing.repo.MatchRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchApplicationService {
    private final MatchRepo matchRepo;
    private final MembershipRepo membershipRepo;
    private final StandingsApplicationService standingsService;

    public MatchId createMatch(String groupId, String player1Id, String player2Id) {
        GroupId groupIdObj = new GroupId(UUID.fromString(groupId));
        PlayerId homePlayerIdObj = new PlayerId(UUID.fromString(player1Id));
        PlayerId awayPlayerIdObj = new PlayerId(UUID.fromString(player2Id));

        if (homePlayerIdObj.equals(awayPlayerIdObj)) {
            throw new IllegalArgumentException("Home and away player must be different");
        }

        membershipRepo.findByGroupIdAndPlayerId(groupIdObj, homePlayerIdObj)
                .orElseThrow(() -> new IllegalArgumentException("Player is not a member of the group: " + player1Id));
        membershipRepo.findByGroupIdAndPlayerId(groupIdObj, awayPlayerIdObj)
                .orElseThrow(() -> new IllegalArgumentException("Player is not a member of the group: " + player2Id));

        Match match = new Match(groupIdObj, homePlayerIdObj, awayPlayerIdObj);
        matchRepo.save(match);

        return match.getId();
    }

    public void completeMatch(MatchId matchId, List<SetDto> completedSets) {

        Optional<Match> match = matchRepo.findById(matchId);
        if (match.isEmpty()) {
            throw new IllegalArgumentException("Match not found: " + matchId);
        }

        List<Set> sets = completedSets.stream()
                .map(dto -> new Set(dto.player1Games(), dto.player2Games()
                        , dto.tiebreakDto() != null
                        ? new TieBreak(dto.tiebreakDto().player1Points(), dto.tiebreakDto().player2Points())
                        : null))
                .toList();

        Match matchObj = match.get();
        matchObj.completeMatch(sets);
        matchRepo.save(matchObj);
        standingsService.updateStandingsForMatch(matchObj);
    }

    public MatchDto getMatchById(MatchId matchId) {
        Optional<Match> match = matchRepo.findById(matchId);
        return match.map(this::convertToDto).orElse(null);
    }

    public List<MatchDto> getMatchesByGroup(GroupId groupId) {
        return matchRepo.findByGroupId(groupId)
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

    public void removeMatch(MatchId matchId) {
        matchRepo.remove(matchId);
    }

    private MatchDto convertToDto(Match match) {
        return new MatchDto(
                match.getId().value().toString(),
                match.getGroupId().value().toString(),
                match.getPlayer1id().value().toString(),
                match.getPlayer2id().value().toString(),
                match.getWinner().value() != null ? match.getWinner().value().toString() : ""
        );
    }
}

