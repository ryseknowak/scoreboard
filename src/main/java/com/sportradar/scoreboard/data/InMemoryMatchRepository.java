package com.sportradar.scoreboard.data;

import com.sportradar.scoreboard.core.ports.MatchRepository;
import com.sportradar.scoreboard.core.ports.types.Match;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@AllArgsConstructor
public class InMemoryMatchRepository implements MatchRepository {

    private Map<Match.MatchSides, Match> matches;

    @Override
    public void save(Match match) {
        matches.put(match.getSides(), match);
    }

    @Override
    public void deleteBySides(Match.MatchSides sides) {
        if (!matches.containsKey(sides)) {
            throw new NoSuchElementException("Match not found");
        }
        matches.remove(sides);
    }

    @Override
    public List<Match> findAll() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean isTeamPlaying(String team) {
        return matches.keySet().stream()
                .anyMatch(sides -> sides.homeTeam().equals(team) || sides.awayTeam().equals(team));
    }

    @Override
    public void updateScore(Match.MatchSides sides, int homeScore, int awayScore) {
        if (!matches.containsKey(sides)) {
            throw new NoSuchElementException("Match not found");
        }
        matches.get(sides).updateScore(homeScore, awayScore);
    }
}
