package com.sportradar.scoreboard.core.ports;

import com.sportradar.scoreboard.core.ports.types.MatchDto;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Service interface for managing live score board for any currently played matches.
 *
 * @see MatchDto for details about the match schema.
 */
public interface LiveScoreBoard {

    /**
     * Starts a new game between two teams.
     *
     * @param homeTeam name of the home team
     * @param awayTeam name of the away team
     * @throws IllegalArgumentException if any of teams are not provided (null or blank) or are already playing
     */
    void startGame(String homeTeam, String awayTeam);

    /**
     * Ends the game between two teams.
     *
     * @param homeTeam name of the home team
     * @param awayTeam name of the away team
     * @throws IllegalArgumentException if any of teams are not provided (null or blank)
     * @throws NoSuchElementException   if the game between the teams is not currently played
     */
    void endGame(String homeTeam, String awayTeam);

    /**
     * Updates the score of the game between two teams.
     *
     * @param homeTeam  name of the home team
     * @param awayTeam  name of the away team
     * @param homeScore new score of the home team
     * @param awayScore new score of the away team
     * @throws IllegalArgumentException if any of teams are not provided (null or blank) or if any of the scores are negative
     * @throws NoSuchElementException   if the game between the teams is not currently played
     */
    void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore);

    /**
     * Returns a summary of all currently played matches.
     * The list is sorted by total score in descending order and by start timestamp in descending order.
     * If two matches have the same total score, the one that started later is first.
     * If two matches have the same total score and start timestamp, the order is not guaranteed.
     *
     * @return list of match summaries
     */
    List<MatchDto> getSummary();
}
