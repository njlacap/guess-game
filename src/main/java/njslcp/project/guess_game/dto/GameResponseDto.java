package njslcp.project.guess_game.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import njslcp.project.guess_game.constant.GuessGameStatusEnum;
import njslcp.project.guess_game.constant.Views;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Response DTO
 * Different views for different response structure
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameResponseDto {

    @JsonView({Views.GameInfoView.class, Views.NewGameDetails.class})
    private String gameId;

    @JsonView(Views.GameSolutionView.class)
    private String gameWord;

    @JsonView({Views.GameSummaryView.class, Views.NewGameDetails.class})
    private String maskedWord;

    @JsonView({Views.GameSummaryView.class, Views.NewGameDetails.class})
    private Integer remainingAttempts;

    @JsonView(Views.GameSummaryView.class)
    private GuessGameStatusEnum status;

    @JsonView({Views.GameSummaryView.class, Views.NewGameDetails.class})
    private List<String> player;

    @JsonView({Views.GameSummaryView.class, Views.NewGameDetails.class})
    private Integer currentPlayer;

    @JsonView({Views.GameSummaryView.class, Views.NewGameDetails.class})
    private LocalDateTime startTime;

    @JsonView({Views.GameSummaryView.class})
    private LocalDateTime endTime;

    @JsonView({Views.GameSummaryView.class})
    private Long duration;

}
