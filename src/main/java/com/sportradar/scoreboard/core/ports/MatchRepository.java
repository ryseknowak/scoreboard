package com.sportradar.scoreboard.core.ports;

import com.sportradar.scoreboard.core.ports.types.MatchDto;

import java.util.List;

public interface MatchRepository {
    void save(MatchDto match);

    void deleteByTeams(String homeTeam, String awayTeam);

    List<MatchDto> findAll();

    boolean isTeamPlaying(String team);

    void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore);
}
