package wooteco.chess.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
	public String joinRoom(@PathVariable String roomName, Model model) {
		model.addAttribute("chessGame", chessGameService.load(roomName));
		return "chess";
	}

	@PostMapping("/room/create")
	public String createRoom(@RequestParam String name, Model model) {
		model.addAttribute("chessGame", chessGameService.create(name));
		return "chess";
	}

	@PostMapping("/api/move")
	@ResponseBody
	public Room movePiece(@RequestBody PieceMoveDto pieceMoveDto) {
		return chessGameService.move(pieceMoveDto);
	}

	@PostMapping("/room/restart")
	public String restart(@RequestParam String name, Model model) {
		model.addAttribute("chessGame", chessGameService.restart(name));
		return "chess";
	}
}
