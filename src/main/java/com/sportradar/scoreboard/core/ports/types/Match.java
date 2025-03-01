package com.sportradar.scoreboard.core.ports.types;

import lombok.*;

import static lombok.AccessLevel.PRIVATE;

@Builder
@Getter
@EqualsAndHashCode
@AllArgsConstructor(access = PRIVATE)
@RequiredArgsConstructor
public class Match {
    private final MatchSides sides;
    private int homeScore;
    private int awayScore;
    private final long startTimestamp = System.nanoTime();

    public void updateScore(int homeScore, int awayScore) {
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
