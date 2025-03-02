package com.sportradar.scoreboard.core.ports.dto;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class MatchDtoTest {

    @ParameterizedTest
    @CsvSource(delimiter = ':', textBlock = """
            1   :   0
            0   :   1
            """)
    void updateScoreWorksCorrectly(int homeScore, int awayScore) {
        var match = MatchDto.builder().build();

        match.updateScore(homeScore, awayScore);

        assertThat(match.getHomeScore()).isEqualTo(homeScore);
        assertThat(match.getAwayScore()).isEqualTo(awayScore);
    }

    @ParameterizedTest
    @CsvSource(delimiter = ':', textBlock = """
            1   :   0
            0   :   1
            1   :   1
            5   :   3
            """)
    void getTotalScoreCorrectlySumsUpHomeAndAwayScores(int homeScore, int awayScore) {
        var match = MatchDto.builder()
                .homeScore(homeScore)
                .awayScore(awayScore)
                .build();

        assertThat(match.getTotalScore()).isEqualTo(homeScore + awayScore);
    }
}