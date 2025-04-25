package njslcp.project.guess_game.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import njslcp.project.guess_game.constant.GuessGameStatusEnum;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Game Class
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Game implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String gameId;

    private String gameWord;

    private String maskedWord;

    private Integer remainingAttempts;

    private GuessGameStatusEnum status;

    private List<String> player;

    private Integer currentPlayer;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Long duration;
}
