package chess.controller;

import chess.dto.RoomResponseDto;
import chess.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/games")
public class GameController {

    final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/{id}")
    public String getGameById() {
        return "chess";
    }

    @GetMapping("/room")
    public ResponseEntity<RoomResponseDto> getRooms() {
        return ResponseEntity.ok(gameService.getRoomNumber());
    }

}
