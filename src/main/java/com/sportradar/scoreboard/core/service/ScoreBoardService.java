package com.sportradar.scoreboard.core.service;

import com.sportradar.scoreboard.core.ports.LiveScoreBoard;
import com.sportradar.scoreboard.core.ports.MatchRepository;
import com.sportradar.scoreboard.core.ports.types.MatchDto;
import lombok.AllArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static io.micrometer.common.util.StringUtils.isBlank;

@AllArgsConstructor
public class ScoreBoardService implements LiveScoreBoard {

    private MatchRepository matchRepository;

    @Override
    public void startGame(String homeTeam, String awayTeam) {
        validateNewMatchCreation(homeTeam, awayTeam);
        matchRepository.save(new MatchDto(homeTeam, awayTeam));
    }

    @Override
    public void endGame(String homeTeam, String awayTeam) {
        validateTeams(homeTeam, awayTeam);
        matchRepository.deleteByTeams(homeTeam, awayTeam);
    }

    @Override
    public void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore) {
        validateTeams(homeTeam, awayTeam);
        if (homeScore < 0 || awayScore < 0) {
            throw new IllegalArgumentException("Score cannot be negative");
        }
        matchRepository.updateScore(homeTeam, awayTeam, homeScore, awayScore);
    }

    @Override
    public List<MatchDto> getSummary() {
        var byTotalScoreReversedComparator = Comparator.comparingInt(MatchDto::getTotalScore).reversed();
        var byStartTimestampReversedComparator = Comparator.comparingLong(MatchDto::getStartTimestamp).reversed();
        return matchRepository.findAll().stream()
                .sorted(byTotalScoreReversedComparator
                        .thenComparing(byStartTimestampReversedComparator))
                .toList();
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
