package njslcp.project.guess_game.service;

import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.OperationFuture;
import njslcp.project.guess_game.helper.MemCacheClient;
import njslcp.project.guess_game.model.Game;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MemCacheService {

    private static final int EXP_DURATION = 86400;

    private MemcachedClient memcachedClient = MemCacheClient.getInstance();

    public Game getGameById(String id) {
        return (Game) memcachedClient.get(id);
    }

    public OperationFuture<Boolean> updateGame(Game game) {

        String id = game.getGameId();
        return memcachedClient.replace(id, EXP_DURATION, game);

    }

    public List<String> getGamePlayerById(String id) {
        String gpId = id+"-GP";
        return Collections.singletonList(memcachedClient.get(gpId).toString());
    }

    public List<String> saveGamePlayer(String id, List<String> gamePlayers) {

        String gpId = id+"-GP";

        memcachedClient.set(gpId, EXP_DURATION, gamePlayers);

        Map<String, List<String>> gamePlayersResult = new HashMap<>();
        gamePlayersResult.put(gpId, gamePlayers);

        return gamePlayersResult.get(id);
    }
}
