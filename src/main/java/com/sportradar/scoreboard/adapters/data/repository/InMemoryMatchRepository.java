package com.sportradar.scoreboard.adapters.data.repository;

import com.sportradar.scoreboard.adapters.data.entity.MatchEntity;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class InMemoryMatchRepository {

    private Map<MatchEntity.MatchSides, MatchEntity> matches;

    public void save(MatchEntity matchEntity) {
        matches.put(matchEntity.getSides(), matchEntity);
    }

    public void deleteBySides(MatchEntity.MatchSides sides) {
        matches.remove(sides);
    }

    public List<MatchEntity> findAll() {
        return matches.values().stream().toList();
    }

    public boolean isTeamPlaying(String team) {
        return matches.keySet().stream()
                .anyMatch(sides -> sides.homeTeam().equals(team) || sides.awayTeam().equals(team));
    }

    public Optional<MatchEntity> getMatchBySides(MatchEntity.MatchSides sides) {
        return Optional.ofNullable(matches.get(sides));
    }
}
