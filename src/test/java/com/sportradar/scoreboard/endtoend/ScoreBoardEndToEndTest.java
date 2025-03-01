package com.sportradar.scoreboard.endtoend;

import com.sportradar.scoreboard.core.ports.LiveScoreBoard;
import com.sportradar.scoreboard.core.ports.types.Match;
import com.sportradar.scoreboard.core.service.ScoreBoardService;
import com.sportradar.scoreboard.data.InMemoryMatchRepository;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ScoreBoardEndToEndTest {

    private static final String MEXICO = "Mexico";
    private static final String CANADA = "Canada";
    private static final String SPAIN = "Spain";
    private static final String BRAZIL = "Brazil";
    private static final String GERMANY = "Germany";
    private static final String FRANCE = "France";
    private static final String URUGUAY = "Uruguay";
    private static final String ITALY = "Italy";
    private static final String ARGENTINA = "Argentina";
    private static final String AUSTRALIA = "Australia";

    private LiveScoreBoard scoreBoard;

    @BeforeEach
    void setUp() {
        scoreBoard = new ScoreBoardService(new InMemoryMatchRepository(new HashMap<>()));
    }

    @Test
    void scoreBoardEndToEndTest() {
        // start games in order
        scoreBoard.startGame(MEXICO, CANADA);
        scoreBoard.startGame(SPAIN, BRAZIL);
        scoreBoard.startGame(GERMANY, FRANCE);
        scoreBoard.startGame(URUGUAY, ITALY);
        scoreBoard.startGame(ARGENTINA, AUSTRALIA);

        // update scores in any order
        scoreBoard.updateScore(GERMANY, FRANCE, 2, 2);
        scoreBoard.updateScore(MEXICO, CANADA, 0, 5);
        scoreBoard.updateScore(SPAIN, BRAZIL, 10, 2);
        scoreBoard.updateScore(ARGENTINA, AUSTRALIA, 3, 1);
        scoreBoard.updateScore(URUGUAY, ITALY, 6, 6);

        // get results snapshot via getSummary and assert results
        assertMatchesSummarySnapshot(scoreBoard.getSummary(),
                getExpectedMatch(URUGUAY, ITALY, 6, 6),
                getExpectedMatch(SPAIN, BRAZIL, 10, 2),
                getExpectedMatch(MEXICO, CANADA, 0, 5),
                getExpectedMatch(ARGENTINA, AUSTRALIA, 3, 1),
                getExpectedMatch(GERMANY, FRANCE, 2, 2));

        // update Argentina vs Australia score and end Spain vs Brazil game
        scoreBoard.updateScore(ARGENTINA, AUSTRALIA, 3, 2);
        scoreBoard.endGame(SPAIN, BRAZIL);

        // get results snapshot via getSummary and assert results
        assertMatchesSummarySnapshot(scoreBoard.getSummary(),
                getExpectedMatch(URUGUAY, ITALY, 6, 6),
                getExpectedMatch(ARGENTINA, AUSTRALIA, 3, 2),
                getExpectedMatch(MEXICO, CANADA, 0, 5),
                getExpectedMatch(GERMANY, FRANCE, 2, 2));
    }

    private static void assertMatchesSummarySnapshot(List<Match> summary, Match... expectedMatches) {
        assertThat(summary)
                .usingRecursiveFieldByFieldElementComparator(RecursiveComparisonConfiguration.builder()
                        .withIgnoredFields("startTimestamp")
                        .build())
                .containsExactly(expectedMatches);
    }

    private static Match getExpectedMatch(String homeTeam, String awayTeam, int homeScore, int awayScore) {
        return Match.builder()
                .sides(new Match.MatchSides(homeTeam, awayTeam))
                .homeScore(homeScore)
                .awayScore(awayScore)
                .build();
    }
}
