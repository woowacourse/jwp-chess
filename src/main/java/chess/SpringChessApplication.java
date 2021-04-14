package chess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@Controller
public class SpringChessApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringChessApplication.class, args);
	}

	@RequestMapping(value = "/")
	public String index() {
		return "index.html";
	}
}
