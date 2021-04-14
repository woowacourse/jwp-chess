package chess;

import chess.dto.requestdto.MoveRequestDto;
import chess.dto.responsedto.TestDto;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.swing.text.html.HTML;

@SpringBootApplication
@RestController
public class SpringChessApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringChessApplication.class, args);
	}

	@GetMapping("/")
	public ModelAndView index() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("index");
		return modelAndView;
	}

	// post("/move", (req, res) -> {
	//            MoveRequestDto moveRequestDto = GSON.fromJson(req.body(), MoveRequestDto.class);
	//            res.status(OK);
	//            return chessService.move(moveRequestDto);
	//        }, GSON::toJson);
	@PostMapping("/move")
	public MoveRequestDto move(@RequestBody MoveRequestDto moveRequestDto) {
		return moveRequestDto;
	}
}
