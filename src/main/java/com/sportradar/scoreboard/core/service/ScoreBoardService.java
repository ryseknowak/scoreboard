package com.sportradar.scoreboard.core.service;

import com.sportradar.scoreboard.core.ports.LiveScoreBoard;
import com.sportradar.scoreboard.core.ports.MatchRepository;
import com.sportradar.scoreboard.core.ports.types.Match;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class ScoreBoardService implements LiveScoreBoard {

    private MatchRepository matchRepository;

    @Override
    public void startGame(String homeTeam, String awayTeam) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void endGame(String homeTeam, String awayTeam) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public List<Match> getSummary() {
        throw new UnsupportedOperationException("Not implemented");
    }
}
