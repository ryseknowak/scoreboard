package com.sportradar.scoreboard.adapters.data.adapter;

import com.sportradar.scoreboard.adapters.data.entity.MatchEntity;
import com.sportradar.scoreboard.adapters.data.mapper.MatchEntityMapper;
import com.sportradar.scoreboard.adapters.data.repository.InMemoryMatchRepository;
import com.sportradar.scoreboard.core.ports.dto.MatchDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchDataAdapterTest {

    @Mock
    private InMemoryMatchRepository matchRepository;
    @Mock
    private MatchEntityMapper matchEntityMapper;
    @InjectMocks
    private MatchDataAdapter matchDataAdapter;

    @Test
    void saveMapsToEntityAndCallsRepositorySave() {
        var matchDto = mock(MatchDto.class);
        var matchEntity = mock(MatchEntity.class);
        when(matchEntityMapper.mapToEntity(matchDto)).thenReturn(matchEntity);

        matchDataAdapter.save(matchDto);

        verify(matchRepository).save(matchEntity);
    }

    @Test
    void deleteByTeamsCallsRepositoryToDeleteByMatchSides() {
        var matchEntity = mock(MatchEntity.class);
        var matchSides = new MatchEntity.MatchSides("home", "away");
        when(matchEntity.getSides()).thenReturn(matchSides);
        when(matchRepository.getMatchBySides(matchSides)).thenReturn(Optional.of(matchEntity));

        matchDataAdapter.deleteByTeams("home", "away");

        verify(matchRepository).deleteBySides(matchSides);
    }

    @Test
    void deleteByTeamsThrowsExceptionWhenMatchDoesNotExist() {
        when(matchRepository.getMatchBySides(new MatchEntity.MatchSides("home", "away"))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> matchDataAdapter.deleteByTeams("home", "away"))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("Match not found");
    }

    @Test
    void findAllCallsRepositoryToGetAllMatchesAndMapsThemToDto() {
        var matchEntity1 = mock(MatchEntity.class);
        var matchEntity2 = mock(MatchEntity.class);
        var matchEntities = List.of(matchEntity1, matchEntity2);
        when(matchRepository.findAll()).thenReturn(matchEntities);
        var matchDto1 = mock(MatchDto.class);
        var matchDto2 = mock(MatchDto.class);
        when(matchEntityMapper.mapToDto(matchEntity1)).thenReturn(matchDto1);
        when(matchEntityMapper.mapToDto(matchEntity2)).thenReturn(matchDto2);

        var resultList = matchDataAdapter.findAll();

        assertThat(resultList).containsExactly(matchDto1, matchDto2);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void isTeamPlayingCallsRepositoryToCheckIfTeamIsPlaying(boolean isPlaying) {
        when(matchRepository.isTeamPlaying("team")).thenReturn(isPlaying);

        var result = matchDataAdapter.isTeamPlaying("team");

        assertThat(result).isEqualTo(isPlaying);
    }
}
