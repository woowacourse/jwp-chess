package chess.web.apicontroller;


import chess.service.GameService;
import chess.web.dto.game.join.JoinRequestDto;
import chess.web.dto.game.room.RoomsResponseDto;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/rooms")
@RestController
public class RoomApiController {

    private final GameService gameService;

    public RoomApiController(final GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public ResponseEntity<RoomsResponseDto> bringRoomData() {
        return ResponseEntity.ok().body(gameService.retrieveRoomsData());
    }

    @PutMapping("/{roomId}/join")
    public ResponseEntity<Object> join(@PathVariable long roomId,
        @RequestBody JoinRequestDto joinRequestDto) {

        gameService.join(roomId, joinRequestDto);
        final long gameId = gameService.bringGameIdByRoomId(roomId);
        final String redirectedUri = String.format("/games/%s", gameId);
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).
            location(URI.create(redirectedUri)).build();
    }

}
