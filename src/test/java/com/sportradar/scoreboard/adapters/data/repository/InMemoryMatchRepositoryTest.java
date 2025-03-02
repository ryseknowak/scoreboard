package com.sportradar.scoreboard.adapters.data.repository;

import com.sportradar.scoreboard.adapters.data.entity.MatchEntity;
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
    HashMap<MatchEntity.MatchSides, MatchEntity> matches;

    @BeforeEach
    void setUp() {
        matches = new HashMap<>();
        repository = new InMemoryMatchRepository(matches);
    }

    @Test
    void saveAddsMatchesToStore() {
        var matchEntity1 = MatchEntity.builder()
                .sides(new MatchEntity.MatchSides(TEAM_1, TEAM_2))
                .build();
        var matchEntity2 = MatchEntity.builder()
                .sides(new MatchEntity.MatchSides(TEAM_3, TEAM_4))
                .build();

        repository.save(matchEntity1);
        repository.save(matchEntity2);

        assertThat(matches)
                .containsEntry(matchEntity1.getSides(), matchEntity1)
                .containsEntry(matchEntity2.getSides(), matchEntity2);
    }

    @Test
    void isTeamPlayingReturnsTrueWhenTeamIsPlayingAndFalseOtherwise() {
        var matchEntity = new MatchEntity(new MatchEntity.MatchSides(TEAM_1, TEAM_2));
        matches.put(matchEntity.getSides(), matchEntity);

        assertThat(repository.isTeamPlaying(TEAM_1)).isTrue();
        assertThat(repository.isTeamPlaying(TEAM_2)).isTrue();
        assertThat(repository.isTeamPlaying(TEAM_3)).isFalse();
    }

    @Test
    void deleteBySidesRemovesMatchFromStore() {
        var matchEntity1 = new MatchEntity(new MatchEntity.MatchSides(TEAM_1, TEAM_2));
        var matchEntity2 = new MatchEntity(new MatchEntity.MatchSides(TEAM_3, TEAM_4));
        matches.put(matchEntity1.getSides(), matchEntity1);
        matches.put(matchEntity2.getSides(), matchEntity2);

        repository.deleteBySides(new MatchEntity.MatchSides(TEAM_1, TEAM_2));

        assertThat(matches)
                .doesNotContainKey(matchEntity1.getSides())
                .containsKey(matchEntity2.getSides());
    }

    @Test
    void findAllReturnsAllMatchesFromTheStore() {
        var matchEntity1 = new MatchEntity(new MatchEntity.MatchSides(TEAM_1, TEAM_2));
        var matchEntity2 = new MatchEntity(new MatchEntity.MatchSides(TEAM_3, TEAM_4));
        matches.put(matchEntity1.getSides(), matchEntity1);
        matches.put(matchEntity2.getSides(), matchEntity2);

        var allMatches = repository.findAll();

        assertThat(allMatches).containsExactlyInAnyOrder(matchEntity1, matchEntity2);
    }
}
