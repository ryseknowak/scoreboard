package com.sportradar.scoreboard.core.ports.dto;

import lombok.*;

import static lombok.AccessLevel.PRIVATE;

@Builder
@Getter
@EqualsAndHashCode
@AllArgsConstructor(access = PRIVATE)
@RequiredArgsConstructor
public class MatchDto {
    private final String homeTeam;
    private final String awayTeam;
    private int homeScore;
    private int awayScore;
    private long startTimestamp;

    public void updateScore(int homeScore, int awayScore) {
        this.homeScore = homeScore;
        this.awayScore = awayScore;
    }

    public int getTotalScore() {
        return homeScore + awayScore;
    }
}
