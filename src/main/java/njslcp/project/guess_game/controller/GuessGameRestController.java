package njslcp.project.guess_game.controller;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import njslcp.project.guess_game.constant.Groups;
import njslcp.project.guess_game.constant.Views;
import njslcp.project.guess_game.dto.GameRequestDto;
import njslcp.project.guess_game.dto.GameResponseDto;
import njslcp.project.guess_game.service.GuessGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Rest controller
 */
@Slf4j
@RestController
@RequestMapping("/game")
public class GuessGameRestController {

    private GuessGameService guessGameService;

    @Autowired
    public GuessGameRestController(GuessGameService guessGameService) {
        this.guessGameService = guessGameService;
    }

    public GuessGameRestController() { }

    @PostMapping(value = "/single", consumes = "application/json")
    @JsonView(Views.NewGameDetails.class)
    public ResponseEntity<GameResponseDto> startSingle( @Validated(Groups.SingleNewGameGroup.class)
                                                        @JsonView(Views.NewGameView.class)
                                                        @RequestBody GameRequestDto gameRequestDto
                                                        , @RequestHeader("X-Request-ID")
                                                        @Pattern(   regexp = "^(\\d{5})$",
                                                                    message = "Request ID format is invalid. Must be 5-digit number") String reqId ) {

        log.info("===== Started a new game - single player. Request-ID: {}", reqId);
        return ResponseEntity.ok(   guessGameService.startNewGame( gameRequestDto ) );
    }

    @PostMapping(value = "/multi", consumes = "application/json")
    @JsonView(Views.NewGameDetails.class)
    public ResponseEntity<GameResponseDto> startMulti(  @Validated(Groups.MultiNewGameGroup.class)
                                                        @JsonView(Views.NewGameView.class)
                                                        @RequestBody GameRequestDto gameRequestDto
                                                        , @RequestHeader("X-Request-ID")
                                                        @Pattern(   regexp = "^(\\d{5})$",
                                                                    message = "Request ID format is invalid. Must be 5-digit number") String reqId ) {

        log.info("===== Started a new game - multi player. Request-ID: {}", reqId);
        return ResponseEntity.ok(   guessGameService.startNewGame( gameRequestDto ) );
    }

    @PutMapping(value = "/{gameId}/guess", consumes = "application/json")
    @JsonView(Views.GameInfoView.class)
    public ResponseEntity<GameResponseDto> guess(   @Validated({Groups.GuessAttemptGroup.class})
                                                    @JsonView(Views.GuessAttemptView.class)
                                                    @RequestBody GameRequestDto gameRequestDTO
                                                    , @PathVariable("gameId") String gameId
                                                    , @RequestHeader("X-Request-ID")
                                                    @Pattern(   regexp = "^(\\d{5})$",
                                                                message = "Request ID format is invalid. Must be 5-digit number") String reqId  ) {

        log.info("===== Guess attempted. Request-ID: {}", reqId);
        return ResponseEntity.ok(   guessGameService.guessAttempt(gameRequestDTO, gameId) );
    }

    @PutMapping(value = "{gameId}/forfeit", consumes = "application/json")
    @JsonView(Views.GameSummaryView.class)
    public ResponseEntity<GameResponseDto> forfeit( @Validated({Groups.ForfeitGroup.class})
                                                    @JsonView(Views.ForfeitView.class)
                                                    @RequestBody GameRequestDto gameRequestDTO
                                                    , @PathVariable("gameId") String gameId
                                                    , @RequestHeader("X-Request-ID")
                                                    @Pattern(   regexp = "^(\\d{5})$",
                                                                message = "Request ID format is invalid. Must be 5-digit number") String reqId) {

        log.info("===== Forfeit the game. Request-ID: {}", reqId);
        return ResponseEntity.ok(   guessGameService.forfeitGame(gameId)    );
    }

    @GetMapping(value = "/{gameId}", consumes = "application/json")
    @JsonView(Views.GameSummaryView.class)
    public ResponseEntity<GameResponseDto> gameDetails( @PathVariable("gameId") String gameId
            , @RequestHeader("X-Request-ID")
                                                        @Pattern(   regexp = "^(\\d{5})$",
                                                                message = "Request ID format is invalid. Must be 5-digit number") String reqId) {

        log.info("===== Get game details. Request-ID: {}", reqId);
        return ResponseEntity.ok(   guessGameService.gameDetails(gameId)   );
    }

    @GetMapping(value = "/leaderboards", consumes = "application/json")
    @JsonView(Views.GameSolutionView.class)
    public ResponseEntity<List<GameResponseDto>> gameDetails(@RequestHeader("X-Request-ID")
                                                             @Pattern(   regexp = "^(\\d{5})$",
                                                                         message = "Request ID format is invalid. Must be 5-digit number") String reqId) {

        log.info("===== Get leaderboards. Request-ID: {}", reqId);
        return ResponseEntity.ok(   guessGameService.leaderboards() );
    }

}