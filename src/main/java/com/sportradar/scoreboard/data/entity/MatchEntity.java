package com.sportradar.scoreboard.data.entity;

import lombok.*;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor(access = PRIVATE)
@RequiredArgsConstructor
public class MatchEntity {
    private final MatchSides sides;
    @Setter
    private int homeScore;
    @Setter
    private int awayScore;
    private final long startTimestamp = System.nanoTime();

    @Builder
    public record MatchSides(String homeTeam, String awayTeam) {
    }
}
