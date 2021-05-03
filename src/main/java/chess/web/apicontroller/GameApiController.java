package chess.web.apicontroller;


import chess.service.GameService;
import chess.web.dto.game.GameResponseDto;
import chess.web.dto.game.move.MoveCheckResponseDto;
import chess.web.dto.game.move.MoveRequestDto;
import chess.web.dto.game.move.MoveResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/games")
@RestController
public class GameApiController {

    private final GameService gameService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public GameApiController(final GameService gameService,
        final SimpMessagingTemplate simpMessagingTemplate) {
        this.gameService = gameService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<GameResponseDto> bringGameData(@PathVariable long gameId) {
        return ResponseEntity.ok().body(gameService.retrieveGameData(gameId));
    }

    @GetMapping("/{gameId}/move/check")
    public ResponseEntity<MoveCheckResponseDto> checkMovement(@PathVariable long gameId,
        @ModelAttribute MoveRequestDto moveRequestDto) {

        return ResponseEntity.ok().body(gameService.checkMovement(gameId, moveRequestDto));
    }

    @PutMapping("/{gameId}/move")
    public ResponseEntity<MoveResponseDto> move(@PathVariable long gameId,
        @RequestBody MoveRequestDto moveRequestDto) {

        final MoveResponseDto moveResponseDto = gameService.move(gameId, moveRequestDto);
        final String subscriptionUrl = String.format("/topic/games/%s/move", gameId);
        simpMessagingTemplate.convertAndSend(subscriptionUrl, moveResponseDto);
        return ResponseEntity.ok().body(moveResponseDto);
    }

}
