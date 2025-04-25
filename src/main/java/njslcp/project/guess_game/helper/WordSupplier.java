package njslcp.project.guess_game.helper;

import lombok.extern.slf4j.Slf4j;
import njslcp.project.guess_game.model.Game;
import njslcp.project.guess_game.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;


@Slf4j
@Component
public class WordSupplier implements Function<String, Game> {

    private GameRepository gameRepository;
    private final Random random = new Random();

    @Autowired
    public WordSupplier(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public WordSupplier() { }

    @Override
    public Game apply(String difficulty) {
        Map<String, Game> gamesMap = gameRepository.getAllGames();

        gamesMap = switch (difficulty) {
            case "hard" ->
                    gamesMap.entrySet().stream().filter(stringGameEntry -> stringGameEntry.getValue().getGameWord().length() == 12).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            case "medium" ->
                    gamesMap.entrySet().stream().filter(stringGameEntry -> stringGameEntry.getValue().getGameWord().length() == 8).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            default ->
                    gamesMap.entrySet().stream().filter(stringGameEntry -> stringGameEntry.getValue().getGameWord().length() == 5).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        };


        // Get all keys of the map, then, to list for index-based structure
        List<String> keys = gamesMap.keySet().stream().toList();

        log.info("keys size: {}", keys.size());

        // Simple randomizer
        // Randomize an int, bound is list size, then, get using the random int
        return gamesMap.get(keys.get(random.nextInt(keys.size())));
    }
}
