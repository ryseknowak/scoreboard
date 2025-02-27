package com.sportradar.scoreboard.core.ports;

import com.sportradar.scoreboard.core.types.Match;

import java.util.List;

/**
 * Service interface for managing live score board for any currently played matches.
 *
 * @see Match for details about the match schema.
 */
public interface LiveScoreBoard {

    void startGame(String homeTeam, String awayTeam);

    void endGame(String homeTeam, String awayTeam);

    void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore);

    List<Match> getSummary();
}
