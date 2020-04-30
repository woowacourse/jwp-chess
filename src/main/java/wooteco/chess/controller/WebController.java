package wooteco.chess.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import wooteco.chess.dto.ChessGameDto;
import wooteco.chess.dto.MoveDto;
import wooteco.chess.dto.RoomDto;
import wooteco.chess.repository.Room;
import wooteco.chess.service.ChessGameService;
import wooteco.chess.service.SpringChessService;

@RestController
public class WebController {
	private final ChessGameService chessGameService;
	private final SpringChessService springChessService;

	public WebController(ChessGameService chessGameService, SpringChessService springChessService) {
		this.chessGameService = chessGameService;
		this.springChessService = springChessService;
	}

	@GetMapping("/")
	public ModelAndView index() {
		return new ModelAndView("index");
	}

	@GetMapping("/api/rooms")
	@ResponseBody
	public List<RoomDto> getRooms() {
		return springChessService.findAllRooms();
	}

	@PostMapping("/join")
	public ModelAndView joinRoom(
			@RequestParam String name) {
		ModelAndView modelAndView = new ModelAndView("chess");
		ChessGameDto chessGameDto = springChessService.load(name);
		modelAndView.addObject("chessGame", chessGameDto);
		return modelAndView;
	}

	@PostMapping("/create")
	public ModelAndView createRoom(
			@RequestParam String name) {
		ModelAndView modelAndView = new ModelAndView("chess");
		ChessGameDto chessGameDto = springChessService.create(name);
		modelAndView.addObject("chessGame", chessGameDto);
		return modelAndView;
	}

	@PostMapping("/api/move")
	@ResponseBody
	public Room movePiece(@RequestBody MoveDto moveDto) {
		return springChessService.move(moveDto);
	}

	@PostMapping("/restart")
	public ModelAndView restart(
			@RequestParam String name) throws SQLException {
		ModelAndView modelAndView = new ModelAndView("chess");
		ChessGameDto chessGameDto = chessGameService.restart(name);
		modelAndView.addObject("chessGame", chessGameDto);
		return modelAndView;
	}
}
