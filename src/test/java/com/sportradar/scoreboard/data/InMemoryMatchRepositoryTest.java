package com.sportradar.scoreboard.data;

import com.sportradar.scoreboard.core.ports.types.Match;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryMatchRepositoryTest {

    InMemoryMatchRepository repository;
    HashMap<Match.MatchSides, Match> matches;

    @BeforeEach
    void setUp() {
        matches = new HashMap<>();
        repository = new InMemoryMatchRepository(matches);
    }

    @Test
    void saveAddsMatchesToStore() {
        var match1 = new Match(new Match.MatchSides("team1", "team2"));
        var match2 = new Match(new Match.MatchSides("team3", "team4"));

        repository.save(match1);
        repository.save(match2);

        assertThat(matches.get(match1.getSides())).isEqualTo(match1);
        assertThat(matches.get(match2.getSides())).isEqualTo(match2);
    }
}
