package njslcp.project.guess_game;

import njslcp.project.guess_game.config.TestConfig;
import njslcp.project.guess_game.config.TestContainerConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import({TestContainerConfig.class, TestConfig.class})
@SpringBootTest
class GuessGameApplicationTests {

	@Test
	void contextLoads() {
	}

}
