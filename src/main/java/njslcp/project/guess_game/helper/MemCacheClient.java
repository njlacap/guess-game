package njslcp.project.guess_game.helper;


import lombok.extern.slf4j.Slf4j;
import net.spy.memcached.MemcachedClient;
import njslcp.project.guess_game.exception.GuessGameException;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@Slf4j
@Component
public class MemCacheClient {

    private static MemcachedClient client;

    private MemCacheClient() {

    }

    public static synchronized MemcachedClient getInstance() {
        if( client == null ) {
            try {
                client = new MemcachedClient(new InetSocketAddress("127.0.0.1", 11211));
            } catch (Exception ex) {
                throw new GuessGameException(ex.getMessage());
            }
        }
        return client;
    }
}
