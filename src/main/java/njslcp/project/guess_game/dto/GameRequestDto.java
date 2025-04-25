package njslcp.project.guess_game.dto;


import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import njslcp.project.guess_game.constant.Groups;
import njslcp.project.guess_game.constant.Views;
import njslcp.project.guess_game.helper.ValuesAllowed;

import java.util.List;

/**
 * Request DTO
 * Different views for different request structure
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameRequestDto {

    @JsonView(Views.GuessAttemptView.class)
    @NotNull(message = "Guess cannot be empty", groups = Groups.GuessAttemptGroup.class)
    @Pattern(regexp = "^[A-Za-z]+$")
    private String guess;

    @JsonView(Views.GuessAttemptView.class)
    @NotNull(message = "Player name cannot be empty", groups = Groups.GuessAttemptGroup.class)
    @Pattern(regexp = "^[A-Za-z]+_[1-9]{1,2}+$")
    private String name;

    @JsonView(Views.NewGameView.class)
    @NotNull(groups = Groups.NewGameGroup.class)
    @Size(min = 2, groups = Groups.MultiNewGameGroup.class)
    @Size(min = 1, max = 1, groups = Groups.SingleNewGameGroup.class)
    private List<String> player;

    @JsonView(Views.NewGameView.class)
    @NotNull(groups = Groups.NewGameGroup.class)
    @ValuesAllowed(values = {"easy","medium","hard"}, groups = {Groups.SingleNewGameGroup.class, Groups.MultiNewGameGroup.class})
    private String difficulty;

    @JsonView(Views.ForfeitView.class)
    @NotNull(groups = Groups.ForfeitGroup.class)
    @AssertTrue
    private Boolean forfeit;


}
