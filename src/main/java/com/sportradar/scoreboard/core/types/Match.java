package com.sportradar.scoreboard.core.types;

import lombok.Builder;

@Builder
public abstract class Match {
    private final MatchSides sides;
    private int homeScore;
    private int awayScore;
    private final long startTimestamp = System.nanoTime();

    public abstract void updateScore(int homeScore, int awayScore);

    public abstract void getTotalScore();

    public record MatchSides(String homeTeam, String awayTeam) {
    }
}
