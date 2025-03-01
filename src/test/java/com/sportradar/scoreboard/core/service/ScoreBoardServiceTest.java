package com.sportradar.scoreboard.core.service;

import com.sportradar.scoreboard.core.ports.MatchRepository;
import com.sportradar.scoreboard.core.ports.types.MatchDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScoreBoardServiceTest {

    @Mock
    private MatchRepository matchRepository;
    @InjectMocks
    private ScoreBoardService scoreBoard;

    @Nested
    class StartGameTests {
        @Test
        void startGameShouldCallRepositoryToSaveMatchWhenNoTeamIsPlayingAlready() {
            var homeTeam = "team1";
            var awayTeam = "team2";
            var argCaptor = ArgumentCaptor.forClass(MatchDto.class);
            when(matchRepository.isTeamPlaying(homeTeam)).thenReturn(false);
            when(matchRepository.isTeamPlaying(awayTeam)).thenReturn(false);

            scoreBoard.startGame(homeTeam, awayTeam);

            verify(matchRepository).save(argCaptor.capture());
            var savedMatch = argCaptor.getValue();
            assertThat(savedMatch)
                    .usingRecursiveComparison()
                    .isEqualTo(new MatchDto(homeTeam, awayTeam));
        }

        @ParameterizedTest
        @CsvSource(value = {"null, away", "home, null", ", away", "home,", "null,null", ","}, nullValues = {"null"})
        void startGameShouldThrowExceptionWhenHomeOrAwayTeamIsNullOrEmpty(String homeTeam, String awayTeam) {
            assertThatThrownBy(() -> scoreBoard.startGame(homeTeam, awayTeam))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Home and away team names must be provided");
        }

        @Test
        void startGameShouldThrowExceptionWhenHomeAndAwayTeamsAreSame() {
            assertThatThrownBy(() -> scoreBoard.startGame("team", "team"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Home and away teams must be different");
        }

        @Test
        void startGameShouldThrowExceptionWhenHomeTeamIsAlreadyPlaying() {
            when(matchRepository.isTeamPlaying("home")).thenReturn(true);

            assertThatThrownBy(() -> scoreBoard.startGame("home", "away"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Team is already playing");
        }

        @Test
        void startGameShouldThrowExceptionWhenAwayTeamIsAlreadyPlaying() {
            when(matchRepository.isTeamPlaying("home")).thenReturn(false);
            when(matchRepository.isTeamPlaying("away")).thenReturn(true);

            assertThatThrownBy(() -> scoreBoard.startGame("home", "away"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Team is already playing");
        }
    }

    @Nested
    class EndGameTests {
        @Test
        void endGameShouldCallRepositoryToDeleteMatch() {
            var homeTeam = "team1";
            var awayTeam = "team2";
            var homeTeamArgCaptor = ArgumentCaptor.forClass(String.class);
            var awayTeamArgCaptor = ArgumentCaptor.forClass(String.class);

            scoreBoard.endGame(homeTeam, awayTeam);

            verify(matchRepository).deleteByTeams(homeTeamArgCaptor.capture(), awayTeamArgCaptor.capture());
            assertThat(homeTeamArgCaptor.getValue()).isEqualTo(homeTeam);
            assertThat(awayTeamArgCaptor.getValue()).isEqualTo(awayTeam);
        }

        @ParameterizedTest
        @CsvSource(value = {"null, away", "home, null", ", away", "home,", "null,null", ","}, nullValues = {"null"})
        void endGameShouldThrowExceptionWhenHomeOrAwayTeamIsNullOrEmpty(String homeTeam, String awayTeam) {
            assertThatThrownBy(() -> scoreBoard.endGame(homeTeam, awayTeam))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Home and away team names must be provided");
        }

        @Test
        void endGameShouldThrowExceptionWhenHomeAndAwayTeamsAreSame() {
            assertThatThrownBy(() -> scoreBoard.endGame("team", "team"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Home and away teams must be different");
        }
    }

    @Nested
    class UpdateScoreTests {

        @Test
        void updateScoreShouldCallRepositoryToUpdateMatchScore() {
            var homeTeam = "team1";
            var awayTeam = "team2";
            var homeScore = 1;
            var awayScore = 2;

            scoreBoard.updateScore(homeTeam, awayTeam, homeScore, awayScore);

            verify(matchRepository).updateScore(homeTeam, awayTeam, homeScore, awayScore);
        }

        @ParameterizedTest
        @CsvSource(delimiter = ':', textBlock = """
                -1  :   0
                 0  :  -1
                """)
        void updateScoreThrowsExceptionWhenNegativeScore(int homeScore, int awayScore) {
            assertThatThrownBy(() -> scoreBoard.updateScore("home", "away", homeScore, awayScore))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Score cannot be negative");
        }
    }

    @Nested
    class GetSummaryTests {

        @Mock
        private MatchDto match1;
        @Mock
        private MatchDto match2;
        @Mock
        private MatchDto match3;
        @Mock
        private MatchDto match4;

        @BeforeEach
        void setUp() {
            when(matchRepository.findAll()).thenReturn(List.of(match1, match2, match3, match4));
        }

        @Test
        void getSummaryShouldSortMatchesByTotalScoreFirst() {
            when(match1.getTotalScore()).thenReturn(1);
            when(match2.getTotalScore()).thenReturn(2);
            when(match3.getTotalScore()).thenReturn(3);
            when(match4.getTotalScore()).thenReturn(4);

            var resultSummary = scoreBoard.getSummary();

            assertThat(resultSummary).containsExactly(match4, match3, match2, match1);
        }

        @Test
        void getSummaryShouldSortMatchesWithSameTotalScoreByStartTimestamp() {
            when(match1.getTotalScore()).thenReturn(1);
            when(match2.getTotalScore()).thenReturn(1);
            when(match3.getTotalScore()).thenReturn(1);
            when(match4.getTotalScore()).thenReturn(1);
            when(match1.getStartTimestamp()).thenReturn(4L);
            when(match2.getStartTimestamp()).thenReturn(3L);
            when(match3.getStartTimestamp()).thenReturn(2L);
            when(match4.getStartTimestamp()).thenReturn(1L);

            var resultSummary = scoreBoard.getSummary();

            assertThat(resultSummary).containsExactly(match1, match2, match3, match4);
        }

        @Test
        void getSummaryShouldSortMatchesByTotalScoreAndStartTimestampWithPriorityOnTotalScore() {
            when(match1.getTotalScore()).thenReturn(1);
            when(match2.getTotalScore()).thenReturn(2);
            when(match3.getTotalScore()).thenReturn(3);
            when(match4.getTotalScore()).thenReturn(2);
            when(match2.getStartTimestamp()).thenReturn(2L);
            when(match4.getStartTimestamp()).thenReturn(1L);

            var resultSummary = scoreBoard.getSummary();

            assertThat(resultSummary).containsExactly(match3, match2, match4, match1);
        }
    }

}