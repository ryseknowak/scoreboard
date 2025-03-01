package com.sportradar.scoreboard.core.service;

import com.sportradar.scoreboard.core.ports.LiveScoreBoard;
import com.sportradar.scoreboard.core.ports.MatchRepository;
import com.sportradar.scoreboard.core.ports.types.Match;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Objects;

import static io.micrometer.common.util.StringUtils.isBlank;

@AllArgsConstructor
public class ScoreBoardService implements LiveScoreBoard {

    private MatchRepository matchRepository;

    @Override
    public void startGame(String homeTeam, String awayTeam) {
        validateNewMatchCreation(homeTeam, awayTeam);
        matchRepository.save(Match.builder()
                .sides(new Match.MatchSides(homeTeam, awayTeam))
                .build());
    }

    @Override
    public void endGame(String homeTeam, String awayTeam) {
        validateTeams(homeTeam, awayTeam);
        matchRepository.deleteBySides(new Match.MatchSides(homeTeam, awayTeam));
    }

    @Override
    public void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore) {
        validateTeams(homeTeam, awayTeam);
        if (homeScore < 0 || awayScore < 0) {
            throw new IllegalArgumentException("Score cannot be negative");
        }
        matchRepository.updateScore(new Match.MatchSides(homeTeam, awayTeam), homeScore, awayScore);
    }

    @Override
    public List<Match> getSummary() {
        throw new UnsupportedOperationException("Not implemented");
    }

    private void validateNewMatchCreation(String homeTeam, String awayTeam) {
        validateTeams(homeTeam, awayTeam);
        if (matchRepository.isTeamPlaying(homeTeam) || matchRepository.isTeamPlaying(awayTeam)) {
            throw new IllegalArgumentException("Team is already playing");
        }
    }

    private static void validateTeams(String homeTeam, String awayTeam) {
        if (isBlank(homeTeam) || isBlank(awayTeam)) {
            throw new IllegalArgumentException("Home and away team names must be provided");
        }
        if (Objects.equals(homeTeam, awayTeam)) {
            throw new IllegalArgumentException("Home and away teams must be different");
        }
    }
}
