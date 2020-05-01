package wooteco.chess.controller.spring;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wooteco.chess.db.entity.PlayerEntity;
import wooteco.chess.db.entity.RoomEntity;
import wooteco.chess.dto.RoomDto;
import wooteco.chess.dto.req.PlayersDto;
import wooteco.chess.service.spring.SpringGameService;
import wooteco.chess.service.spring.SpringPlayerService;

import java.util.List;

@Controller
@RequestMapping("/room")
public class RoomController {
	private final SpringPlayerService playerService;
	private final SpringGameService gameService;

	public RoomController(SpringPlayerService playerService, SpringGameService gameService) {
		this.playerService = playerService;
		this.gameService = gameService;
	}

	@GetMapping
	public String createNewGame() {
		return "createNewGame";
	}

	@PostMapping
	@ResponseBody
	public ResponseEntity<Object> createNewGame(@RequestBody PlayersDto playersDto) {
		try {
			long player1Id = playerService.save(
					new PlayerEntity(playersDto.getPlayer1Name(), playersDto.getPlayer1Password(), "white"));
			long player2Id = playerService.save(
					new PlayerEntity(playersDto.getPlayer2Name(), playersDto.getPlayer2Password(), "black"));
			long roomId = gameService.createRoom(player1Id, player2Id);
			gameService.createBoard(roomId);
			return ResponseEntity
					.status(200)
					.body(new RoomDto(roomId, playersDto.getPlayer1Name(), playersDto.getPlayer2Name()));
		} catch (Exception e) {
			return ResponseEntity.status(400).body(e.getMessage());
		}
	}

	@GetMapping("/{id}")
	public String start(@PathVariable int id, Model model) {
		model.addAttribute("id", id);
		return "play";
	}

	@GetMapping("/all")
	public ResponseEntity<List<RoomEntity>> showRooms() {
		List<RoomEntity> list = gameService.getRooms();
		for (RoomEntity roomEntity : list) {
			System.out.println(roomEntity.getId());
		}
		return ResponseEntity.ok(gameService.getRooms());
	}
}
