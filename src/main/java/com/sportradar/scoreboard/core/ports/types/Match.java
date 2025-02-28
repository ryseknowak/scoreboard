package com.sportradar.scoreboard.core.ports.types;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class Match {
    private final MatchSides sides;
    private int homeScore;
    private int awayScore;
    private final long startTimestamp = System.nanoTime();

    public void updateScore(int homeScore, int awayScore) {
        if (homeScore < 0 || awayScore < 0) {
            throw new IllegalArgumentException("Score cannot be negative");
        }
        this.homeScore = homeScore;
        this.awayScore = awayScore;
    }

    public int getTotalScore() {
        return homeScore + awayScore;
    }

    @Builder
    public record MatchSides(String homeTeam, String awayTeam) {
    }
}
