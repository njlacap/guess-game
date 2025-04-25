package njslcp.project.guess_game.util;

/**
 * String utility for masking and splitting
 */
public class StringUtil {

    private StringUtil() { }

    public static String maskWord(String word) {
        return "_ ".repeat(word.length()).trim();
    }
    public static String splitBySpace(String word) {
        return word.replaceAll(""," ").trim();
    }
}
