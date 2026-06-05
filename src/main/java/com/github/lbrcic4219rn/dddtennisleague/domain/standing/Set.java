package com.github.lbrcic4219rn.dddtennisleague.domain.standing;

public record Set(
        int player1Games,
        int player2Games,
        TieBreak tieBreak
) {
    public Set {
        if (player1Games < 0 || player2Games < 0) {
            throw new IllegalArgumentException("Number of games cannot be negative");
        }
        if (player1Games > 7 || player2Games > 7) {
            throw new IllegalArgumentException("Number of games cannot exceed 7");
        }

        if ((tieBreak != null && tieBreak.player1Points() != 0 && tieBreak.player2Points() != 0 ) && (player1Games != 6 || player2Games != 6)) {
            throw new IllegalArgumentException("Tie-break can only occur if both players have won 6 games");
        }

        int maxGames = Math.max(player1Games, player2Games);
        int minGames = Math.min(player1Games, player2Games);

        if (maxGames == 7 && minGames < 5) {
            throw new IllegalArgumentException("A set cannot be won 7-5 or less");
        }
        if (maxGames == 6 && minGames > 4) {
            throw new IllegalArgumentException("A set cannot be won 6-5 or more");
        }
    }

    public boolean isPlayer1Winner() {
        return (player1Games >= 6 && player1Games - player2Games >= 2) ||
                (tieBreak != null && tieBreak.isPlayer1Winner());
    }
}