package chess;

import chess.dto.responsedto.TestDto;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SpringChessApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringChessApplication.class, args);
	}

	@GetMapping("/")
	public String index() {
		return "index";
	}

	// post("/move", (req, res) -> {
	//            MoveRequestDto moveRequestDto = GSON.fromJson(req.body(), MoveRequestDto.class);
	//            res.status(OK);
	//            return chessService.move(moveRequestDto);
	//        }, GSON::toJson);
	@GetMapping("/move")
	public TestDto move() {
		return new TestDto("마갸", 100);
	}
}
