package wooteco.chess.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import wooteco.chess.dto.RoomDto;
import wooteco.chess.service.ChessGameService;

@Controller
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
}
