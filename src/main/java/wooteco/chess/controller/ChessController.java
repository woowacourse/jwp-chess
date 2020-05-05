package wooteco.chess.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import wooteco.chess.domain.piece.Color;
import wooteco.chess.domain.position.Position;
import wooteco.chess.service.GameManagerService;

@Controller
public class ChessController {
	private GameManagerService gameManagerService;

	public ChessController(GameManagerService gameManagerService) {
		this.gameManagerService = gameManagerService;
	}

	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("roomNumbers", gameManagerService.getAllRoomNo());
		return "index";
	}

	@GetMapping("/board/{roomNo}")
	public String board(Model model, @PathVariable int roomNo) {
		Color currentTurn = gameManagerService.getCurrentTurn(roomNo);

		try {
			model.addAttribute("roomNo", roomNo);
			model.addAttribute("piecesDto", gameManagerService.getBoardDto(roomNo));
			model.addAttribute("turn", currentTurn.name());
			model.addAttribute("scores", gameManagerService.calculateEachScore(roomNo));
		} catch (RuntimeException e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("redirectUrl", "/");
			return "error";
		}
		return "board";
	}

	@GetMapping("/start")
	public String start() {
		int roomNo = gameManagerService.newGame();
		return "redirect:/board/" + roomNo;
	}

	@PostMapping("/move")
	public String move(Model model, @RequestParam Map<String, String> param) {
		String target = param.get("target");
		String destination = param.get("destination");
		int roomNo = Integer.parseInt(param.get("roomNo"));

		try {
			gameManagerService.move(Position.of(target), Position.of(destination), roomNo);
		} catch (RuntimeException e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("redirectUrl", "/board/" + roomNo);
			return "error";
		}
		if (!gameManagerService.isKingAlive(roomNo)) {
			Color currentTurn = gameManagerService.getCurrentTurn(roomNo);
			model.addAttribute("winner", currentTurn.reverse());
			gameManagerService.deleteGame(roomNo);
			return "end";
		}
		return "redirect:/board/" + roomNo;
	}

	@GetMapping("/end")
	public String end(@RequestParam int roomNo) {
		gameManagerService.deleteGame(roomNo);
		return "end";
	}
}
