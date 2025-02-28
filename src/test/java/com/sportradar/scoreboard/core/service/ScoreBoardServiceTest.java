package com.sportradar.scoreboard.core.service;

import com.sportradar.scoreboard.core.ports.MatchRepository;
import com.sportradar.scoreboard.core.ports.types.Match;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
            var argCaptor = ArgumentCaptor.forClass(Match.class);
            when(matchRepository.isTeamPlaying(homeTeam)).thenReturn(false);
            when(matchRepository.isTeamPlaying(awayTeam)).thenReturn(false);

            scoreBoard.startGame(homeTeam, awayTeam);

            verify(matchRepository).save(argCaptor.capture());
            var savedMatch = argCaptor.getValue();
            assertThat(savedMatch.getStartTimestamp()).isNotZero();
            assertThat(savedMatch)
                    .usingRecursiveComparison()
                    .ignoringFields("startTimestamp")
                    .isEqualTo(getExpectedMatch(homeTeam, awayTeam));
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
            var argCaptor = ArgumentCaptor.forClass(Match.MatchSides.class);

            scoreBoard.endGame(homeTeam, awayTeam);

            verify(matchRepository).deleteBySides(argCaptor.capture());
            assertThat(argCaptor.getValue())
                    .usingRecursiveComparison()
                    .isEqualTo(Match.MatchSides.builder()
                            .homeTeam(homeTeam)
                            .awayTeam(awayTeam)
                            .build());
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

    private static Match getExpectedMatch(String homeTeam, String awayTeam) {
        return Match.builder()
                .sides(Match.MatchSides.builder()
                        .homeTeam(homeTeam)
                        .awayTeam(awayTeam)
                        .build())
                .build();
    }

}