package com.github.lbrcic4219rn.dddtennisleague.domain.standing;

public record TieBreak(
        int player1Points,
        int player2Points
) {
    public TieBreak {
        if (player1Points < 0 || player2Points < 0) {
            throw new IllegalArgumentException("Points cannot be negative");
        }

        int maxPoints = Math.max(player1Points, player2Points);
        int minPoints = Math.min(player1Points, player2Points);

        if (maxPoints < 7) {
            throw new IllegalArgumentException("Winner must have at least 7 points");
        }

        int margin = maxPoints - minPoints;
        if (margin < 2) {
            throw new IllegalArgumentException("Tie-break must end with at least a 2-point margin");
        }

        if (maxPoints > 7 && margin != 2) {
            throw new IllegalArgumentException("Extended tie-break must end with an exact 2-point margin");
        }
    }

    public boolean isPlayer1Winner() {
        return player1Points > player2Points;
    }
}
