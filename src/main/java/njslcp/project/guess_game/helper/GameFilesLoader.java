package njslcp.project.guess_game.helper;

import lombok.extern.slf4j.Slf4j;
import njslcp.project.guess_game.exception.GuessGameException;
import njslcp.project.guess_game.model.Game;
import org.springframework.beans.factory.annotation.Value;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

@Slf4j
public class GameFilesLoader {

    private static final String FILE_NAME = "dictionary.txt";
    private static final Map<String, Game> gameStorage = new HashMap<>();

    private GameFilesLoader() {   }

    public static synchronized Map<String, Game> getLoadedGames() {

        if(gameStorage.isEmpty()) {
            try (Stream<String> lines = Files.lines(Paths.get(Objects.requireNonNull(GameFilesLoader.class.getClassLoader().getResource(FILE_NAME)).toURI()))) {
                lines.forEach(word -> {
                    log.info("word: {}", word);
                    String pid = UUID.randomUUID().toString();
                    gameStorage.put(pid, Game.builder().gameId(pid).gameWord(word).build());
                });
            } catch (Exception ex) {
                log.error("Error: {}", ex.getMessage());
                throw new GuessGameException(ex.getMessage());
            }
        }
        return gameStorage;
    }


}
