package com.sportradar.scoreboard.data;

import com.sportradar.scoreboard.core.ports.types.MatchDto;
import com.sportradar.scoreboard.data.entity.MatchEntity;
import com.sportradar.scoreboard.data.mapper.MatchEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class InMemoryMatchRepositoryTest {

    private static final String TEAM_1 = "team1";
    private static final String TEAM_2 = "team2";
    private static final String TEAM_3 = "team3";
    private static final String TEAM_4 = "team4";

    InMemoryMatchRepository repository;
    HashMap<MatchEntity.MatchSides, MatchEntity> matches;
    MatchEntityMapper matchEntityMapper;

    @BeforeEach
    void setUp() {
        matches = new HashMap<>();
        matchEntityMapper = mock(MatchEntityMapper.class);
        repository = new InMemoryMatchRepository(matches, matchEntityMapper);
    }

    @Test
    void saveAddsMatchesToStore() {
        var matchDto1 = mock(MatchDto.class);
        var matchDto2 = mock(MatchDto.class);
        var matchEntity1 = MatchEntity.builder()
                .sides(new MatchEntity.MatchSides(TEAM_1, TEAM_2))
                .build();
        var matchEntity2 = MatchEntity.builder()
                .sides(new MatchEntity.MatchSides(TEAM_3, TEAM_4))
                .build();
        when(matchEntityMapper.mapToEntity(matchDto1)).thenReturn(matchEntity1);
        when(matchEntityMapper.mapToEntity(matchDto2)).thenReturn(matchEntity2);

        repository.save(matchDto1);
        repository.save(matchDto2);

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
    void deleteByTeamsRemovesMatchFromStore() {
        var matchEntity1 = new MatchEntity(new MatchEntity.MatchSides(TEAM_1, TEAM_2));
        var matchEntity2 = new MatchEntity(new MatchEntity.MatchSides(TEAM_3, TEAM_4));
        matches.put(matchEntity1.getSides(), matchEntity1);
        matches.put(matchEntity2.getSides(), matchEntity2);

        repository.deleteByTeams(TEAM_1, TEAM_2);

        assertThat(matches)
                .doesNotContainKey(matchEntity1.getSides())
                .containsKey(matchEntity2.getSides());
    }

    @Test
    void deleteByTeamsThrowsExceptionWhenMatchDoesNotExist() {
        var existingMatchEntity = new MatchEntity(new MatchEntity.MatchSides(TEAM_1, TEAM_2));
        matches.put(existingMatchEntity.getSides(), existingMatchEntity);

        assertThatThrownBy(() -> repository.deleteByTeams(TEAM_3, TEAM_4))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("MatchDto not found");
    }

    @Test
    void updateScoreThrowsExceptionWhenMatchDoesNotExist() {
        var existingMatchEntity = new MatchEntity(new MatchEntity.MatchSides(TEAM_1, TEAM_2));
        matches.put(existingMatchEntity.getSides(), existingMatchEntity);

        assertThatThrownBy(() -> repository.updateScore(TEAM_3, TEAM_4, 1, 2))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("MatchDto not found");
    }

    @Test
    void updateScoreUpdatesMatchScore() {
        var matchEntity = new MatchEntity(new MatchEntity.MatchSides(TEAM_1, TEAM_2));
        var matchSides = matchEntity.getSides();
        matches.put(matchSides, matchEntity);

        repository.updateScore(TEAM_1, TEAM_2, 1, 2);

        assertThat(matches.get(matchSides).getHomeScore()).isEqualTo(1);
        assertThat(matches.get(matchSides).getAwayScore()).isEqualTo(2);
    }

    @Test
    void findAllReturnsAllMatchesMappedToDtos() {
        var matchEntity1 = mock(MatchEntity.class);
        var matchEntity2 = mock(MatchEntity.class);
        var matchEntitySides1 = mock(MatchEntity.MatchSides.class);
        var matchEntitySides2 = mock(MatchEntity.MatchSides.class);
        var matchDto1 = mock(MatchDto.class);
        var matchDto2 = mock(MatchDto.class);
        matches.put(matchEntitySides1, matchEntity1);
        matches.put(matchEntitySides2, matchEntity2);
        when(matchEntityMapper.mapToDto(matchEntity1)).thenReturn(matchDto1);
        when(matchEntityMapper.mapToDto(matchEntity2)).thenReturn(matchDto2);

        var allMatches = repository.findAll();

        assertThat(allMatches).containsExactlyInAnyOrder(matchDto1, matchDto2);
    }
}
