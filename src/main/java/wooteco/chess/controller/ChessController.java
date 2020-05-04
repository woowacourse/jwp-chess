package wooteco.chess.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import wooteco.chess.dto.request.MoveRequestDto;
import wooteco.chess.dto.response.ChessGameParser;
import wooteco.chess.service.ChessGameService;

@Controller
@RequestMapping("/chess")
public class ChessController {
	private final ChessGameService chessGameService;

	public ChessController(ChessGameService chessGameService) {
		this.chessGameService = chessGameService;
	}

	@GetMapping("/{roomId}")
	public ModelAndView loadChessGame(@PathVariable Long roomId) {
		Map<String, Object> model = new HashMap<>();
		try {
			model = chessGameService.loadBoard(roomId).parseModel();
		} catch (IllegalArgumentException e) {
			model.put("error", e.getMessage());
		}
		return new ModelAndView("game", model);
	}

	@PostMapping("/{roomId}")
	public ModelAndView newChessGame(@PathVariable Long roomId) {
		ChessGameParser chessGameParser = chessGameService.createNewChessGame(roomId);
		Map<String, Object> model = chessGameParser.parseModel();
		return new ModelAndView("game", model);
	}

	@DeleteMapping("/{roomId}")
	public String deleteChessGame(@PathVariable Long roomId) {
		chessGameService.deleteGame(roomId);
		return "redirect:/";
	}

	@PostMapping("/{roomId}/move")
	public ModelAndView move(@RequestBody @Valid MoveRequestDto moveRequestDto) {
		Long roomId = moveRequestDto.getRoomId();

		Map<String, Object> model;
		try {
			model = chessGameService.movePiece(moveRequestDto).parseModel();
		} catch (IllegalArgumentException e) {
			model = chessGameService.loadBoard(roomId).parseModel();
			model.put("error", e.getMessage());
		}

		if (chessGameService.isGameOver(roomId)) {
			return new ModelAndView(String.format("redirect:/result/%d", roomId));
		}
		return new ModelAndView("game", model);
	}
}
