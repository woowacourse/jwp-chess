package chess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@Controller
public class SpringChessApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(SpringChessApplication.class, args);
	}

	@GetMapping("/")
	public String index() {
		return "game";
	}
}
