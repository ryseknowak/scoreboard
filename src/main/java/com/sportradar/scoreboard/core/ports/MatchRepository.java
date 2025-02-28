package com.sportradar.scoreboard.core.ports;

import com.sportradar.scoreboard.core.ports.types.Match;

import java.util.List;
import java.util.Optional;

public interface MatchRepository {
    void save(Match match);

    void deleteBySides(Match.MatchSides sides);

    Optional<Match> findBySides(Match.MatchSides sides);

    List<Match> findAll();

    boolean isTeamPlaying(String team);
}
