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
import wooteco.chess.service.ChessGameService;

@RestController
public class SpringController {
	private ChessGameService chessGameService;

	public SpringController(ChessGameService chessGameService) {
		this.chessGameService = chessGameService;
	}

	@GetMapping("/")
	public ModelAndView index() {
		return new ModelAndView("index");
	}

	@GetMapping("/api/rooms")
	@ResponseBody
	public List<RoomDto> getRooms() throws SQLException {
		return chessGameService.findAllRooms();
	}

	@PostMapping("/join")
	public ModelAndView joinRoom(
			@RequestParam(value = "name") String name) throws SQLException {
		ModelAndView modelAndView = new ModelAndView("chess");
		ChessGameDto chessGameDto = chessGameService.load(name);
		modelAndView.addObject("chessGame", chessGameDto);
		return modelAndView;
	}

	@PostMapping("/create")
	public ModelAndView createRoom(
			@RequestParam(value = "name") String name) throws SQLException {
		ModelAndView modelAndView = new ModelAndView("chess");
		ChessGameDto chessGameDto = chessGameService.create(name);
		modelAndView.addObject("chessGame", chessGameDto);
		return modelAndView;
	}

	@PostMapping("/move")
	public ModelAndView movePiece(
			@RequestBody MoveDto moveDto) throws SQLException {
		ModelAndView modelAndView = new ModelAndView("chess");
		ChessGameDto chessGameDto = chessGameService.move(moveDto.getRoomName(), moveDto.getSource(),
				moveDto.getTarget());
		modelAndView.addObject("chessGame", chessGameDto);
		return modelAndView;
	}

	@PostMapping("/restart")
	public ModelAndView restart(
			@RequestParam(value = "name") String name) throws SQLException {
		ModelAndView modelAndView = new ModelAndView("chess");
		ChessGameDto chessGameDto = chessGameService.restart(name);
		modelAndView.addObject("chessGame", chessGameDto);
		return modelAndView;
	}
}
