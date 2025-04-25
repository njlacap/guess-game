package njslcp.project.guess_game.constant;

public enum GuessGameStatusEnum {

    IN_PROGRESS("in-progress"),
    WON("won"),
    LOST("lost"),
    NOT_STARTED("not-started"),
    FORFEITED("forfeited");

    public final String value;

    private GuessGameStatusEnum(String value) {
        this.value = value;
    }

}
