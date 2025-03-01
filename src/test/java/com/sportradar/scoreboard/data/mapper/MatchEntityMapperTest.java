package com.sportradar.scoreboard.data.mapper;

import com.sportradar.scoreboard.core.ports.types.MatchDto;
import com.sportradar.scoreboard.data.entity.MatchEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MatchEntityMapperTest {

    MatchEntityMapper mapper = MatchEntityMapper.INSTANCE;

    @Test
    void mapToEntityIgnoresStartTimestampMapping() {
        MatchDto dto = MatchDto.builder()
                .homeTeam("home")
                .awayTeam("away")
                .homeScore(1)
                .awayScore(2)
                .startTimestamp(123L) // this should be ignored and replaced with current time
                .build();

        MatchEntity entity = mapper.mapToEntity(dto);

        assertThat(entity.getStartTimestamp())
                .isNotZero()
                .isNotEqualTo(dto.getStartTimestamp());
        assertThat(entity)
                .usingRecursiveComparison()
                .ignoringFields("startTimestamp")
                .isEqualTo(getExpectedEntity());
    }

    private static MatchEntity getExpectedEntity() {
        return MatchEntity.builder()
                .sides(new MatchEntity.MatchSides("home", "away"))
                .homeScore(1)
                .awayScore(2)
                .build();
    }
}