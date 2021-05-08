package chess.web.apicontroller;


import chess.service.GameService;
import chess.web.dto.game.GameResponseDto;
import chess.web.dto.game.join.JoinRequestDto;
import chess.web.dto.game.room.RoomsResponseDto;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
    private final SimpMessagingTemplate simpMessagingTemplate;

    public RoomApiController(final GameService gameService,
        final SimpMessagingTemplate simpMessagingTemplate) {
        this.gameService = gameService;
        this.simpMessagingTemplate = simpMessagingTemplate;
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

        final GameResponseDto gameResponseDto = gameService.retrieveGameData(gameId);
        final String subscriptionUrl = String.format("/topic/games/%s/join", gameId);
        simpMessagingTemplate.convertAndSend(subscriptionUrl, gameResponseDto);

        final String redirectedUri = String.format("/games/%s", gameId);
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).
            location(URI.create(redirectedUri)).build();
    }

}
