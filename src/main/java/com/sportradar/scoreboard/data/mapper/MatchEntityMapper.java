package com.sportradar.scoreboard.data.mapper;

import com.sportradar.scoreboard.core.ports.types.MatchDto;
import com.sportradar.scoreboard.data.entity.MatchEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MatchEntityMapper {

    MatchEntityMapper INSTANCE = Mappers.getMapper(MatchEntityMapper.class);

    @Mapping(target = "sides", source = "dto")
    @Mapping(target = "dto.startTimestamp", ignore = true)
    MatchEntity mapToEntity(MatchDto dto);

    @Mapping(target = "homeTeam", source = "sides.homeTeam")
    @Mapping(target = "awayTeam", source = "sides.awayTeam")
    MatchDto mapToDto(MatchEntity entity);

    default MatchEntity.MatchSides mapSides(MatchDto dto) {
        return new MatchEntity.MatchSides(dto.getHomeTeam(), dto.getAwayTeam());
    }
}
