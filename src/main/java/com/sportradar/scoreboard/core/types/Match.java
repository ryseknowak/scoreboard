package com.sportradar.scoreboard.core.types;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class Match {
    private final MatchSides sides;
    private int homeScore;
    private int awayScore;
    private final long startTimestamp = System.nanoTime();

    public void updateScore(int homeScore, int awayScore) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public int getTotalScore() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public record MatchSides(String homeTeam, String awayTeam) {
    }
}
