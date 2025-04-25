package njslcp.project.guess_game.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.GenericContainer;

@TestConfiguration(proxyBeanMethods = false)
public class TestContainerConfig {

    @Bean
    GenericContainer<?> memcachedContainer() {
        return new GenericContainer<>("memcached:1.6.38-alpine")
            .withExposedPorts(11211);
    }
}
