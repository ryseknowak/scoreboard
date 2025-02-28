package com.sportradar.scoreboard.data;

import com.sportradar.scoreboard.core.ports.types.Match;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryMatchRepositoryTest {

    private static final String TEAM_1 = "team1";
    private static final String TEAM_2 = "team2";
    private static final String TEAM_3 = "team3";
    private static final String TEAM_4 = "team4";

    InMemoryMatchRepository repository;
    HashMap<Match.MatchSides, Match> matches;

    @BeforeEach
    void setUp() {
        matches = new HashMap<>();
        repository = new InMemoryMatchRepository(matches);
    }

    @Test
    void saveAddsMatchesToStore() {
        var match1 = new Match(new Match.MatchSides(TEAM_1, TEAM_2));
        var match2 = new Match(new Match.MatchSides(TEAM_3, TEAM_4));

        repository.save(match1);
        repository.save(match2);

        assertThat(matches)
                .containsEntry(match1.getSides(), match1)
                .containsEntry(match2.getSides(), match2);
    }

    @Test
    void isTeamPlayingReturnsTrueWhenTeamIsPlayingAndFalseOtherwise() {
        var match1 = new Match(new Match.MatchSides(TEAM_1, TEAM_2));
        matches.put(match1.getSides(), match1);

        assertThat(repository.isTeamPlaying(TEAM_1)).isTrue();
        assertThat(repository.isTeamPlaying(TEAM_2)).isTrue();
        assertThat(repository.isTeamPlaying(TEAM_3)).isFalse();
    }
}
