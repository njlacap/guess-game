package njslcp.project.guess_game.repository;

import njslcp.project.guess_game.exception.GuessGameException;
import njslcp.project.guess_game.model.Game;

import java.util.Map;

public interface GameRepository {

    Map<String, Game> getAllGames() throws GuessGameException;
    Game getGameById(String id) throws GuessGameException;
    Game updateGame(Game game) throws GuessGameException;

}
