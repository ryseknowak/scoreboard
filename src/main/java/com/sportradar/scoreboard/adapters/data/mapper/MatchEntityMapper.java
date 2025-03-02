package com.sportradar.scoreboard.adapters.data.mapper;

import com.sportradar.scoreboard.adapters.data.entity.MatchEntity;
import com.sportradar.scoreboard.core.ports.dto.MatchDto;
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
