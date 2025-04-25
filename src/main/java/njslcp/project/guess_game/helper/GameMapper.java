package njslcp.project.guess_game.helper;

import njslcp.project.guess_game.dto.GameResponseDto;
import njslcp.project.guess_game.model.Game;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Mapper using MapStruct
 * Enabled annotation processor in pom for this
 */
@Mapper(componentModel = "spring")
public interface GameMapper {

    @Mapping(target = "gameId", source = "dto.gameId")
    @Mapping(target = "maskedWord", source = "dto.maskedWord")
    @Mapping(target = "remainingAttempts", source = "dto.remainingAttempts")
    @Mapping(target = "status", source = "dto.status")
    @Mapping(target = "player", source = "dto.player")
    @Mapping(target = "currentPlayer", source = "dto.currentPlayer")
    @Mapping(target = "startTime", source = "dto.startTime")
    @Mapping(target = "endTime", source = "dto.endTime")
    Game gameResponseDtoToGame(GameResponseDto dto);

    @Mapping(target = "gameId", source = "entity.gameId")
    @Mapping(target = "maskedWord", source = "entity.maskedWord")
    @Mapping(target = "remainingAttempts", source = "entity.remainingAttempts")
    @Mapping(target = "status", source = "entity.status")
    @Mapping(target = "player", source = "entity.player")
    @Mapping(target = "currentPlayer", source = "entity.currentPlayer")
    @Mapping(target = "startTime", source = "entity.startTime")
    @Mapping(target = "endTime", source = "entity.endTime")
    GameResponseDto gameToGameResponseDto(Game entity);

    List<GameResponseDto> map(List<Game> gameList);
}
