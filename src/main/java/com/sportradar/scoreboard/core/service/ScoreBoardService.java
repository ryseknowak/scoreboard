package com.sportradar.scoreboard.core.service;

import com.sportradar.scoreboard.core.ports.dto.MatchDto;
import com.sportradar.scoreboard.core.ports.incoming.LiveScoreBoard;
import com.sportradar.scoreboard.core.ports.outgoing.MatchDataPort;
import lombok.AllArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static io.micrometer.common.util.StringUtils.isBlank;

@AllArgsConstructor
public class ScoreBoardService implements LiveScoreBoard {

    private MatchDataPort matchDataPort;

    @Override
    public void startGame(String homeTeam, String awayTeam) {
        validateNewMatchCreation(homeTeam, awayTeam);
        matchDataPort.save(new MatchDto(homeTeam, awayTeam));
    }

    @Override
    public void endGame(String homeTeam, String awayTeam) {
        validateTeams(homeTeam, awayTeam);
        matchDataPort.deleteByTeams(homeTeam, awayTeam);
    }

    @Override
    public void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore) {
        validateTeams(homeTeam, awayTeam);
        if (homeScore < 0 || awayScore < 0) {
            throw new IllegalArgumentException("Score cannot be negative");
        }
        matchDataPort.updateScore(homeTeam, awayTeam, homeScore, awayScore);
    }

    @Override
    public List<MatchDto> getSummary() {
        var byTotalScoreReversedComparator = Comparator.comparingInt(MatchDto::getTotalScore).reversed();
        var byStartTimestampReversedComparator = Comparator.comparingLong(MatchDto::getStartTimestamp).reversed();
        return matchDataPort.findAll().stream()
                .sorted(byTotalScoreReversedComparator
                        .thenComparing(byStartTimestampReversedComparator))
                .toList();
    }

    private void validateNewMatchCreation(String homeTeam, String awayTeam) {
        validateTeams(homeTeam, awayTeam);
        if (matchDataPort.isTeamPlaying(homeTeam) || matchDataPort.isTeamPlaying(awayTeam)) {
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
