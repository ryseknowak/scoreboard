package com.sportradar.scoreboard.core.ports;

import com.sportradar.scoreboard.core.ports.types.Match;

import java.util.List;

public interface MatchRepository {
    void save(Match match);

    void deleteBySides(Match.MatchSides sides);

    List<Match> findAll();

    boolean isTeamPlaying(String team);

    void updateScore(Match.MatchSides sides, int homeScore, int awayScore);
}
