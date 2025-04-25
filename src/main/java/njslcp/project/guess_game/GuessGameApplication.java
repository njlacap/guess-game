package njslcp.project.guess_game;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "njslcp.project.guess_game")
@EnableAutoConfiguration
public class GuessGameApplication {

	public static void main(String[] args) {
		SpringApplication.run(GuessGameApplication.class, args);
	}

}
