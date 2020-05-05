package wooteco.chess.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import wooteco.chess.entity.RoomEntity;
import wooteco.chess.service.ChessGameService;

@Controller
public class RoomController {
	@Autowired
	private ChessGameService chessGameService;

	@GetMapping("/")
	public ModelAndView index() {
		Map<String, Object> model = new HashMap<>();
		List<RoomEntity> roomEntities = chessGameService.loadRooms();
		model.put("rooms", roomEntities);
		return new ModelAndView("index", model);
	}

	@PostMapping("/new")
	public String createRoom(@RequestParam(defaultValue = "") String name) {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("방 제목이 없습니다.");
		}
		RoomEntity roomEntity = chessGameService.createRoom(name);
		return String.format("redirect:/chess/%s", roomEntity.getId());
	}
}
