package com.sportradar.scoreboard.adapters.data.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class MatchEntityTest {

    @Test
    void twoMatchesCreatedOneByOneHaveGrowingTimestamps() {
        var match1 = MatchEntity.builder().build();
        var match2 = MatchEntity.builder().build();

        assertThat(match1.getStartTimestamp()).isLessThan(match2.getStartTimestamp());
    }

}