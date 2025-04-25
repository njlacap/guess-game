package njslcp.project.guess_game.repository;

import njslcp.project.guess_game.exception.GuessGameException;

import java.util.List;
import java.util.Map;

public interface GamePlayerRepository {

    List<String> getGamePlayerById(String id) throws GuessGameException;
    List<String> saveGamePlayer(String id, List<String> gamePlayer) throws GuessGameException;

}
