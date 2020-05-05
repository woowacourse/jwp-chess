package wooteco.chess.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import wooteco.chess.domain.Result;
import wooteco.chess.domain.chessboard.Board;
import wooteco.chess.domain.chesspiece.Piece;
import wooteco.chess.domain.position.Position;
import wooteco.chess.dto.RoomDto;
import wooteco.chess.service.ChessService;

@Controller
public class SpringController {

	private final ChessService chessService;

	public SpringController(ChessService chessService) {
		this.chessService = chessService;
	}

	@GetMapping("/")
	public String index(Model model) {
		List<RoomDto> roomDtos = chessService.findAllRoom();
		model.addAttribute("rooms", roomDtos);
		return "lobby";
	}

	@GetMapping("/create")
	@ResponseBody
	public Long createRoom(@RequestParam("title") String title) {
		RoomDto roomDto = chessService.insertRoom(title);
		return roomDto.getId();
	}

	@GetMapping("/open")
	public String showBoard() {
		return "board";
	}

	@GetMapping("/init")
	@ResponseBody
	public Map<String, Object> init(@RequestParam("roomId") Long roomId) {
		return makeModel(chessService.findByRoomId(roomId));
	}

	@PutMapping("/move")
	@ResponseBody
	public Map<String, Object> move(@RequestParam("roomId") Long roomId,
		@RequestParam("start") String start, @RequestParam("target") String target) {

		Map<String, Object> model = new HashMap<>();
		chessService.move(roomId, Position.of(start), Position.of(target));
		model.put("start", start);
		model.put("target", target);
		model.put("isEnd", chessService.isEnd(roomId));
		return model;
	}

	@GetMapping("/findWinningTeam")
	@ResponseBody
	public Map<String, Object> isEnd(@RequestParam("roomId") Long roomId) {
		Map<String, Object> model = new HashMap<>();
		model.put("winningTeam", chessService.findWinningTeam(roomId));
		return model;
	}

	@GetMapping("/delete")
	@ResponseBody
	public void delete(@RequestParam("roomId") Long roomId) {
		chessService.delete(roomId);
	}

	@GetMapping("/status")
	@ResponseBody
	public Map<String, Object> status(@RequestParam("roomId") Long roomId) {
		Result result = chessService.status(roomId);
		Map<String, Object> model = new HashMap<>();
		model.put("blackTeamScore", result.getBlackTeamScore());
		model.put("whiteTeamScore", result.getWhiteTeamScore());
		return model;
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler({IllegalArgumentException.class, UnsupportedOperationException.class})
	@ResponseBody
	public String handleException(RuntimeException e) {
		return e.getMessage();
	}

	private Map<String, Object> makeModel(Board board) {
		Map<String, Object> model = new HashMap<>();
		for (Piece piece : board.findAll()) {
			model.put(piece.getPosition().getString(), piece.getName());
		}
		return model;
	}
}