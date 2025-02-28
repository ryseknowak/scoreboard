package com.sportradar.scoreboard.data;

import com.sportradar.scoreboard.core.ports.MatchRepository;
import com.sportradar.scoreboard.core.ports.types.Match;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class InMemoryMatchRepository implements MatchRepository {

    private Map<Match.MatchSides, Match> matches;

    @Override
    public void save(Match match) {
        matches.put(match.getSides(), match);
    }

    @Override
    public void deleteBySides(Match.MatchSides sides) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Optional<Match> findBySides(Match.MatchSides sides) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public List<Match> findAll() {
        throw new UnsupportedOperationException("Not implemented");
    }
}
