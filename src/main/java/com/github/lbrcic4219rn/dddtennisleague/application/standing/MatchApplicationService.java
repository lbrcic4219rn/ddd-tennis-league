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

@Service
@RequiredArgsConstructor
public class MatchApplicationService {
    private final MatchRepo matchRepo;
    private final MembershipRepo membershipRepo;
    private final StandingsApplicationService standingsService;

    public MatchId createMatch(GroupId groupId, PlayerId player1Id, PlayerId player2Id) {

        if (player1Id.equals(player2Id)) {
            throw new IllegalArgumentException("Players must be different");
        }
        membershipRepo.findByGroupIdAndPlayerId(groupId, player1Id)
                .orElseThrow(() -> new IllegalArgumentException("Player is not a member of the group: " + player1Id));
        membershipRepo.findByGroupIdAndPlayerId(groupId, player2Id)
                .orElseThrow(() -> new IllegalArgumentException("Player is not a member of the group: " + player2Id));
        Match match = new Match(groupId, player1Id, player2Id);
        matchRepo.save(match);

        return match.getId();
    }

    public void completeMatch(MatchId matchId, List<SetDto> completedSets) {

        Optional<Match> match = matchRepo.findById(matchId);
        if (match.isEmpty()) {
            throw new IllegalArgumentException("Match not found: " + matchId);
        }

        List<Set> sets = completedSets.stream()
                .map(this::getSetFromDto)
                .toList();

        Match matchObj = match.get();
        matchObj.completeMatch(sets);
        matchRepo.save(matchObj);
        standingsService.updateStandingsForMatch(matchObj);
    }

    public MatchDto getMatchById(MatchId matchId) throws IllegalAccessError {
        Match match = matchRepo.findById(matchId).orElseThrow(IllegalAccessError::new);
        return getMatchDto(match);
    }

    public List<MatchDto> getMatchesByGroup(GroupId groupId) {
        return matchRepo.findByGroupId(groupId)
                .stream()
                .map(this::getMatchDto)
                .toList();
    }

    public List<MatchDto> getAllMatches() {
        return matchRepo.findAll()
                .stream()
                .map(this::getMatchDto)
                .toList();
    }

    public void removeMatch(MatchId matchId) {
        matchRepo.findById(matchId).orElseThrow(IllegalAccessError::new);
        matchRepo.remove(matchId);
    }

    private MatchDto getMatchDto(Match match) {
        return new MatchDto(
                match.getId().value().toString(),
                match.getGroupId().value().toString(),
                match.getPlayer1id().value().toString(),
                match.getPlayer2id().value().toString(),
                match.getWinner().value() != null ? match.getWinner().value().toString() : ""
        );
    }

    private Set getSetFromDto(SetDto dto) {
        return new Set(dto.player1Games(), dto.player2Games()
                , dto.tiebreakDto() != null
                ? new TieBreak(dto.tiebreakDto().player1Points(), dto.tiebreakDto().player2Points())
                : null);
    }
}

