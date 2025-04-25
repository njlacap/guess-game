package njslcp.project.guess_game.repository;

import lombok.extern.slf4j.Slf4j;
import njslcp.project.guess_game.exception.GuessGameException;
import njslcp.project.guess_game.helper.GameFilesLoader;
import njslcp.project.guess_game.model.Game;
import njslcp.project.guess_game.service.MemCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
public class GameRepositoryImpl implements GameRepository {

    private final MemCacheService memCacheService;

    @Autowired
    public GameRepositoryImpl(MemCacheService memCacheService) {
        this.memCacheService = memCacheService;
    }

    @Override
    public Map<String, Game> getAllGames() throws GuessGameException {
        Map<String, Game> games = new HashMap<>();

        try {
            GameFilesLoader.getLoadedGames().forEach((key, value) -> games.put(key, memCacheService.getGameById(key)));
        } catch (Exception ex) {
            log.error("{}",ex.getMessage());
            throw new GuessGameException(ex.getMessage());
        }

        return games;
    }

    @Override
    public Game getGameById(String id) throws GuessGameException {
        Game game;

        try {
            game = memCacheService.getGameById(id);
        } catch (Exception ex) {
            log.error("{}",ex.getMessage());
            throw new GuessGameException(ex.getMessage());
        }

        if(Objects.isNull(game)) {
            throw new GuessGameException("Game is non-existent. Please check your game id");
        }

        return game;
    }

    @Override
    public Game updateGame(Game game) throws GuessGameException {

        boolean result;

        try {
            result = memCacheService.updateGame(game).get();
        } catch (InterruptedException | ExecutionException ex) {
            Thread.currentThread().interrupt();
            throw new GuessGameException(ex.getMessage());
        }

        if (!result) {
            throw new GuessGameException("Failed to save game. Please try again later.");
        }

        return getGameById(game.getGameId());
    }


}
