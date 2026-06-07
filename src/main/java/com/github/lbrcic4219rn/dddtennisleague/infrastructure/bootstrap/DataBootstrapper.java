package com.github.lbrcic4219rn.dddtennisleague.infrastructure.bootstrap;

import com.github.lbrcic4219rn.dddtennisleague.application.league.LeagueApplicationService;
import com.github.lbrcic4219rn.dddtennisleague.application.standing.MatchApplicationService;
import com.github.lbrcic4219rn.dddtennisleague.application.league.MembershipApplicationService;
import com.github.lbrcic4219rn.dddtennisleague.application.player.PlayerApplicationService;
import com.github.lbrcic4219rn.dddtennisleague.application.standing.StandingsApplicationService;
import com.github.lbrcic4219rn.dddtennisleague.application.standing.dto.SetDto;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.id.LeagueId;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.SkillLevel;
import com.github.lbrcic4219rn.dddtennisleague.domain.player.id.PlayerId;
import com.github.lbrcic4219rn.dddtennisleague.domain.standing.id.MatchId;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataBootstrapper implements CommandLineRunner {

    private final PlayerApplicationService playerService;
    private final LeagueApplicationService leagueService;
    private final MembershipApplicationService membershipService;
    private final MatchApplicationService matchService;
    private final StandingsApplicationService standingsService;

    public DataBootstrapper(
            PlayerApplicationService playerService,
            LeagueApplicationService leagueService,
            MembershipApplicationService membershipService,
            MatchApplicationService matchService,
            StandingsApplicationService standingsService) {
        this.playerService = playerService;
        this.leagueService = leagueService;
        this.membershipService = membershipService;
        this.matchService = matchService;
        this.standingsService = standingsService;
    }

    @Override
    public void run(String @NonNull ... args) {
        bootstrapPlayers();
    }

    private void bootstrapPlayers() {
        try {
            // Create 6 sample players
            PlayerId player1 = playerService.registerPlayer(
                    "Roger",
                    "Federer",
                    "roger@example.com",
                    "+1-555-0001",
                    LocalDate.of(1981, 8, 8)
            );

            PlayerId player2 = playerService.registerPlayer(
                    "Rafael",
                    "Nadal",
                    "rafael@example.com",
                    "+1-555-0002",
                    LocalDate.of(1986, 6, 3)
            );

            PlayerId player3 = playerService.registerPlayer(
                    "Novak",
                    "Djokovic",
                    "novak@example.com",
                    "+1-555-0003",
                    LocalDate.of(1987, 5, 22)
            );

            PlayerId player4 = playerService.registerPlayer(
                    "Andy",
                    "Murray",
                    "andy@example.com",
                    "+1-555-0004",
                    LocalDate.of(1987, 5, 15)
            );

            PlayerId player5 = playerService.registerPlayer(
                    "Dominic",
                    "Thiem",
                    "dominic@example.com",
                    "+1-555-0005",
                    LocalDate.of(1996, 9, 3)
            );

            PlayerId player6 = playerService.registerPlayer(
                    "Matteo",
                    "Berrettini",
                    "matteo@example.com",
                    "+1-555-0006",
                    LocalDate.of(1994, 4, 12)
            );

            // Create a league
            Instant now = Instant.now();
            Instant startDate = now.minus(30, ChronoUnit.DAYS);
            Instant endDate = now.plus(30, ChronoUnit.DAYS);

            LeagueId leagueId = leagueService.createLeague(
                    "Spring Tennis League 2026",
                    startDate,
                    endDate
            );

            // Create groups for different skill levels
            String beginnerGroupId = leagueService.createGroup(leagueId, SkillLevel.BEGINNER).value().toString();
            String intermediateGroupId = leagueService.createGroup(leagueId, SkillLevel.INTERMEDIATE).value().toString();
            String advancedGroupId = leagueService.createGroup(leagueId, SkillLevel.ADVANCED).value().toString();

            // Add players to groups (Invariant: one group per player per league)
            membershipService.joinGroup(player1.value().toString(), advancedGroupId);
            membershipService.joinGroup(player2.value().toString(), advancedGroupId);
            membershipService.joinGroup(player3.value().toString(), advancedGroupId);
            membershipService.joinGroup(player4.value().toString(), intermediateGroupId);
            membershipService.joinGroup(player5.value().toString(), intermediateGroupId);
            membershipService.joinGroup(player6.value().toString(), beginnerGroupId);

            // Create leaderboards
            standingsService.createLeaderboard(advancedGroupId);
            standingsService.createLeaderboard(intermediateGroupId);
            standingsService.createLeaderboard(beginnerGroupId);

            // Create some matches
            bootstrapAdvancedGroupMatches(advancedGroupId, player1.value().toString(), player2.value().toString(),
                    player3.value().toString());
            bootstrapIntermediateGroupMatches(intermediateGroupId, player4.value().toString(), player5.value().toString());

            System.out.println("✓ Bootstrap data populated successfully!");
            System.out.println("  - 6 players created");
            System.out.println("  - 1 league created with 3 groups (BEGINNER, INTERMEDIATE, ADVANCED)");
            System.out.println("  - 6 memberships created");
            System.out.println("  - 3 leaderboards created");
            System.out.println("  - Sample matches created with results");
        } catch (Exception e) {
            System.err.println("Error bootstrapping data: " + e.getMessage());
        }
    }

    private void bootstrapAdvancedGroupMatches(String groupId, String player1Id, String player2Id, String player3Id) {
        List<MatchId> matchIds = new ArrayList<>();

        // Match 1: Player1 vs Player2 (Player1 wins)
        MatchId match1Id = matchService.createMatch(groupId, player1Id, player2Id);
        matchService.completeMatch(match1Id, List.of(
                new SetDto(6, 4, null),
                new SetDto(6, 3, null)
        ));
        matchIds.add(match1Id);

        // Match 2: Player2 vs Player3 (Player3 wins)
        MatchId match2Id = matchService.createMatch(groupId, player2Id, player3Id);
        matchService.completeMatch(match2Id, List.of(
                new SetDto(4, 6, null),
                new SetDto(3, 6, null)
        ));
        matchIds.add(match2Id);

        // Match 3: Player1 vs Player3 (Player1 wins)
        MatchId match3Id = matchService.createMatch(groupId, player1Id, player3Id);
        matchService.completeMatch(match3Id, List.of(
                new SetDto(6, 4, null),
                new SetDto(6, 3, null)
        ));
        matchIds.add(match3Id);

        System.out.println("  - Advanced group: " + matchIds.size() + " matches created");
    }

    private void bootstrapIntermediateGroupMatches(String groupId, String player1Id, String player2Id) {
        List<MatchId> matchIds = new ArrayList<>();

        // Match 1: Player1 vs Player2 (Player1 wins)
        MatchId match1Id = matchService.createMatch(groupId, player1Id, player2Id);
        matchService.completeMatch(match1Id, List.of(
                new SetDto(6, 4, null),
                new SetDto(6, 3, null)
        ));
        matchIds.add(match1Id);

        System.out.println("  - Intermediate group: " + matchIds.size() + " matches created");
    }
}

