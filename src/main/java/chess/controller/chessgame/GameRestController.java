package chess.controller.chessgame;

import chess.chessgame.domain.room.game.ChessGameManager;
import chess.chessgame.domain.room.game.board.position.Position;
import chess.chessgame.domain.room.user.User;
import chess.service.ChessService;
import chess.service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@RequestMapping("/game")
public class GameRestController {
    private final ChessService chessService;
    private final RoomService roomService;

    public GameRestController(ChessService chessService, RoomService roomService) {
        this.chessService = chessService;
        this.roomService = roomService;
    }

    @GetMapping("{gameId:[\\d]+}/score")
    public ResponseEntity<ScoreResponseDto> getScore(@PathVariable long gameId) {
        return ResponseEntity.ok(new ScoreResponseDto(chessService.getStatistics(gameId)));
    }

    @GetMapping(value = "{gameId:[\\d]+}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChessGameResponseDto> loadGame(@PathVariable long gameId) {
        ChessGameManager load = chessService.load(gameId);
        return ResponseEntity.ok(new ChessGameResponseDto(load));
    }

    @PutMapping("move")
    public ResponseEntity<MoveResponseDto> movePiece(@RequestBody MoveRequestDto moveMessage, @SessionAttribute User user) {
        if (roomService.isAuthorityUser(user, moveMessage.getRoomId())) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "방에 입장할 수 없습니다.");
        }

        chessService.move(moveMessage.getGameId(), user.getColor(), Position.of(moveMessage.getFrom()), Position.of(moveMessage.getTo()));
        return ResponseEntity.ok(new MoveResponseDto(chessService.isEnd(moveMessage.getGameId()), chessService.nextColor(moveMessage.getGameId())));
    }
}
