package njslcp.project.guess_game.exception;

import lombok.Getter;

@Getter
public class GuessGameException extends RuntimeException {

    String requestId;

    public GuessGameException(String message) {
        super(message);
    }

    public GuessGameException(String requestId, String message) {
        super(message);
        this.requestId = requestId;
    }


}
