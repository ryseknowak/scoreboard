package com.sportradar.scoreboard.core.ports;

import com.sportradar.scoreboard.core.ports.types.MatchDto;

import java.util.List;

/**
 * Service interface for managing live score board for any currently played matches.
 *
 * @see MatchDto for details about the match schema.
 */
public interface LiveScoreBoard {

    void startGame(String homeTeam, String awayTeam);

    void endGame(String homeTeam, String awayTeam);

    void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore);

    List<MatchDto> getSummary();
}
