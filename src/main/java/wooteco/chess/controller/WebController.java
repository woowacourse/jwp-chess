package wooteco.chess.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import wooteco.chess.dto.ChessGameDto;
import wooteco.chess.dto.PieceMoveDto;
import wooteco.chess.dto.RoomNameDto;
import wooteco.chess.repository.Room;
import wooteco.chess.service.ChessGameService;

@Controller
public class WebController {
	private final ChessGameService chessGameService;

	public WebController(ChessGameService chessGameService) {
		this.chessGameService = chessGameService;
	}

	@GetMapping("/")
	public String index() {
		return "index.html";
	}

	@GetMapping("/api/rooms")
	@ResponseBody
	public List<RoomNameDto> getRooms() {
		return chessGameService.findAllRooms();
	}

	@GetMapping("/room/{roomName}")
	public ModelAndView joinRoom(@PathVariable String roomName) {
		ModelAndView modelAndView = new ModelAndView("chess");
		ChessGameDto chessGameDto = chessGameService.load(roomName);
		modelAndView.addObject("chessGame", chessGameDto);
		return modelAndView;
	}

	@PostMapping("/create")
	public ModelAndView createRoom(@RequestParam String name) {
		ModelAndView modelAndView = new ModelAndView("chess");
		ChessGameDto chessGameDto = chessGameService.create(name);
		modelAndView.addObject("chessGame", chessGameDto);
		return modelAndView;
	}

	@PostMapping("/api/move")
	@ResponseBody
	public Room movePiece(@RequestBody PieceMoveDto pieceMoveDto) {
		return chessGameService.move(pieceMoveDto);
	}

	@PostMapping("/restart")
	public ModelAndView restart(@RequestParam String name) {
		ModelAndView modelAndView = new ModelAndView("chess");
		ChessGameDto chessGameDto = chessGameService.restart(name);
		modelAndView.addObject("chessGame", chessGameDto);
		return modelAndView;
	}
}
