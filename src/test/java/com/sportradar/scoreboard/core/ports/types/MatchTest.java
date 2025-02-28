package com.sportradar.scoreboard.core.ports.types;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class MatchTest {

    @ParameterizedTest
    @CsvSource(delimiter = ':', textBlock = """
            1   :   0
            0   :   1
            """)
    void updateScoreWorksCorrectlyWhenPositiveScore(int homeScore, int awayScore) {
        var match = Match.builder().build();

        match.updateScore(homeScore, awayScore);

        assertThat(match.getHomeScore()).isEqualTo(homeScore);
        assertThat(match.getAwayScore()).isEqualTo(awayScore);
    }

    @ParameterizedTest
    @CsvSource(delimiter = ':', textBlock = """
            -1  :   0
             0  :  -1
            """)
    void updateScoreThrowsExceptionWhenNegativeScore(int homeScore, int awayScore) {
        var match = Match.builder().build();

        assertThatThrownBy(() -> match.updateScore(homeScore, awayScore))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Score cannot be negative");
    }

    @ParameterizedTest
    @CsvSource(delimiter = ':', textBlock = """
            1   :   0
            0   :   1
            1   :   1
            5   :   3
            """)
    void getTotalScoreCorrectlySumsUpHomeAndAwayScores(int homeScore, int awayScore) {
        var match = Match.builder()
                .homeScore(homeScore)
                .awayScore(awayScore)
                .build();

        assertThat(match.getTotalScore()).isEqualTo(homeScore + awayScore);
    }

    @Test
    void twoMatchesCreatedOneByOneHaveGrowingTimestamps() {
        var match1 = Match.builder().build();
        var match2 = Match.builder().build();

        assertThat(match1.getStartTimestamp()).isLessThan(match2.getStartTimestamp());
    }
}