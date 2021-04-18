package chess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SpringChessApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringChessApplication.class, args);
	}
}
