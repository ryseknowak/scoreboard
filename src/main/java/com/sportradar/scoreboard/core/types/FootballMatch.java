package com.sportradar.scoreboard.core.types;

import lombok.Builder;

@Builder
public class FootballMatch extends Match {

    @Override
    public void updateScore(int homeScore, int awayScore) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void getTotalScore() {
        throw new UnsupportedOperationException("Not implemented");
    }
}
