package chess.controller.spring;

import chess.dto.CommonDto;
import chess.dto.GameListDto;
import chess.service.ChessService;
import chess.service.RoomDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/room")
public class RoomController {
    private final ChessService service;

    public RoomController(ChessService service) {
        this.service = service;
    }

    @GetMapping("/list")
    public ResponseEntity<CommonDto<GameListDto>> loadGames() {
        return ResponseEntity.ok().body(service.loadGameList());
    }

    @PostMapping("/name")
    public ResponseEntity<CommonDto<RoomDto>> saveRoomName(@RequestBody String roomName) {
        return ResponseEntity.ok().body(service.saveRoom(roomName));
    }
}
