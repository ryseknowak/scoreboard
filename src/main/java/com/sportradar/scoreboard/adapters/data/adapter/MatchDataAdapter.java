package com.sportradar.scoreboard.adapters.data.adapter;

import com.sportradar.scoreboard.adapters.data.entity.MatchEntity;
import com.sportradar.scoreboard.adapters.data.mapper.MatchEntityMapper;
import com.sportradar.scoreboard.adapters.data.repository.InMemoryMatchRepository;
import com.sportradar.scoreboard.core.ports.dto.MatchDto;
import com.sportradar.scoreboard.core.ports.outgoing.MatchDataPort;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.NoSuchElementException;

@AllArgsConstructor
public class MatchDataAdapter implements MatchDataPort {

    private MatchEntityMapper matchEntityMapper;
    private InMemoryMatchRepository matchRepository;

    @Override
    public void save(MatchDto matchDto) {
        matchRepository.save(matchEntityMapper.mapToEntity(matchDto));
    }

    @Override
    public void deleteByTeams(String homeTeam, String awayTeam) {
        matchRepository.getMatchBySides(new MatchEntity.MatchSides(homeTeam, awayTeam))
                .ifPresentOrElse(matchEntity -> matchRepository.deleteBySides(matchEntity.getSides()),
                        () -> {
                            throw new NoSuchElementException("Match not found");
                        });
    }

    @Override
    public void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore) {
        var matchEntity = matchRepository.getMatchBySides(new MatchEntity.MatchSides(homeTeam, awayTeam))
                .orElseThrow(() -> new NoSuchElementException("Match not found"));
        matchEntity.setHomeScore(homeScore);
        matchEntity.setAwayScore(awayScore);
        matchRepository.save(matchEntity);
    }

    @Override
    public List<MatchDto> findAll() {
        return matchRepository.findAll().stream()
                .map(matchEntityMapper::mapToDto)
                .toList();
    }

    @Override
    public boolean isTeamPlaying(String team) {
        return matchRepository.isTeamPlaying(team);
    }
}
