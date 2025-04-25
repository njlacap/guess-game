package njslcp.project.guess_game.exception;

public class AlreadyGuessedException extends RuntimeException {
    public AlreadyGuessedException(String message) {
        super(message);
    }
}
