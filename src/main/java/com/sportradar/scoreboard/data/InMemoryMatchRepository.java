package com.sportradar.scoreboard.data;

import com.sportradar.scoreboard.core.ports.MatchRepository;
import com.sportradar.scoreboard.core.ports.types.MatchDto;
import com.sportradar.scoreboard.data.entity.MatchEntity;
import com.sportradar.scoreboard.data.mapper.MatchEntityMapper;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@AllArgsConstructor
public class InMemoryMatchRepository implements MatchRepository {

    private Map<MatchEntity.MatchSides, MatchEntity> matches;
    private MatchEntityMapper matchEntityMapper;

    @Override
    public void save(MatchDto matchDto) {
        var newEntity = matchEntityMapper.mapToEntity(matchDto);
        matches.put(newEntity.getSides(), newEntity);
    }

    @Override
    public void deleteByTeams(String homeTeam, String awayTeam) {
        var sides = new MatchEntity.MatchSides(homeTeam, awayTeam);
        if (!matches.containsKey(sides)) {
            throw new NoSuchElementException("MatchDto not found");
        }
        matches.remove(sides);
    }

    @Override
    public List<MatchDto> findAll() {
        return matches.values().stream()
                .map(matchEntityMapper::mapToDto)
                .toList();
    }

    @Override
    public boolean isTeamPlaying(String team) {
        return matches.keySet().stream()
                .anyMatch(sides -> sides.homeTeam().equals(team) || sides.awayTeam().equals(team));
    }

    @Override
    public void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore) {
        var matchEntity = matches.get(new MatchEntity.MatchSides(homeTeam, awayTeam));
        if (matchEntity == null) {
            throw new NoSuchElementException("MatchDto not found");
        }
        matchEntity.setHomeScore(homeScore);
        matchEntity.setAwayScore(awayScore);
    }
}
