package com.sportradar.scoreboard.core.ports;

import com.sportradar.scoreboard.core.ports.types.MatchDto;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Repository interface for managing matches.
 *
 * @see MatchDto for more details about the match schema.
 */
public interface MatchRepository {

    /**
     * Creates new match.
     *
     * @param match match to be saved
     */
    void save(MatchDto match);

    /**
     * Deletes match by teams.
     *
     * @param homeTeam name of the home team
     * @param awayTeam name of the away team
     * @throws NoSuchElementException if the match between the teams is not found
     */
    void deleteByTeams(String homeTeam, String awayTeam);

    /**
     * Retrieves all matches.
     *
     * @return list of all matches currently stored
     */
    List<MatchDto> findAll();

    /**
     * Checks if the team is currently playing.
     *
     * @param team name of the team
     * @return true if the team is playing, false otherwise
     */
    boolean isTeamPlaying(String team);

    /**
     * Updates the score of the match between two teams.
     *
     * @param homeTeam  name of the home team
     * @param awayTeam  name of the away team
     * @param homeScore new score of the home team
     * @param awayScore new score of the away team
     * @throws NoSuchElementException if the match between the teams is not found
     */
    void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore);
}
