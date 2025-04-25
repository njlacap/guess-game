package njslcp.project.guess_game.repository;

import lombok.extern.slf4j.Slf4j;
import net.spy.memcached.MemcachedClient;
import njslcp.project.guess_game.exception.GuessGameException;
import njslcp.project.guess_game.helper.GameFilesLoader;
import njslcp.project.guess_game.helper.MemCacheClient;
import njslcp.project.guess_game.model.Game;
import njslcp.project.guess_game.service.MemCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
public class GamePlayerRepositoryImpl implements GamePlayerRepository {

    private final MemCacheService memCacheService;

    @Autowired
    public GamePlayerRepositoryImpl(MemCacheService memCacheService) {
        this.memCacheService = memCacheService;
    }

    @Override
    public List<String> getGamePlayerById(String id) throws GuessGameException {
        return memCacheService.getGamePlayerById(id);
    }

    @Override
    public List<String> saveGamePlayer(String id, List<String> gamePlayers) throws GuessGameException {
        return memCacheService.saveGamePlayer(id, gamePlayers);
    }

}
