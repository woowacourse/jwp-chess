package chess.controller.web;

import chess.chessgame.domain.room.game.ChessGameManager;
import chess.controller.web.dto.ChessGameResponseDto;
import chess.controller.web.dto.MoveRequestDto;
import chess.controller.web.dto.MoveResponseDto;
import chess.controller.web.dto.ScoreResponseDto;
import chess.service.ChessService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
public class GameRestController {
    private final ChessService chessService;

    public GameRestController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("{gameId}/score")
    public ResponseEntity<ScoreResponseDto> getScore(@PathVariable long gameId) {
        return ResponseEntity.ok(new ScoreResponseDto(chessService.getStatistics(gameId)));
    }

    @GetMapping(value = "{gameId:[\\d]+}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChessGameResponseDto> loadGame(@PathVariable long gameId) {
        ChessGameManager load = chessService.load(gameId);
        return ResponseEntity.ok(new ChessGameResponseDto(load));
    }

    @PutMapping("move")
    public ResponseEntity<MoveResponseDto> movePiece(@RequestBody MoveRequestDto moveMessage) {
        chessService.move(moveMessage);
        return ResponseEntity.ok(new MoveResponseDto(chessService. isEnd(moveMessage.getGameId()), chessService.nextColor(moveMessage.getGameId())));
    }
}
