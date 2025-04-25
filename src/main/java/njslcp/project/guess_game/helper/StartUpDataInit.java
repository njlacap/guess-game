package njslcp.project.guess_game.helper;

import lombok.extern.slf4j.Slf4j;
import net.spy.memcached.MemcachedClient;
import njslcp.project.guess_game.constant.GuessGameStatusEnum;
import njslcp.project.guess_game.exception.GuessGameException;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StartUpDataInit implements ApplicationListener<ContextRefreshedEvent> {

    // Memcached data initialization
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        MemcachedClient memcachedClient = MemCacheClient.getInstance();

        try {
            GameFilesLoader.getLoadedGames().forEach((key, value) -> {
                log.info("word: {}", value.getGameWord());
                value.setStatus(GuessGameStatusEnum.NOT_STARTED);
                memcachedClient
                        .set(key, 86400
                                , value);
            });
        } catch (Exception ex) {
            log.error("Error: {}", ex.getMessage());
            throw new GuessGameException(ex.getMessage());
        }

    }

}