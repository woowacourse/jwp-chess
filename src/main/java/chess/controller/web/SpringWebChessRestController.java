package chess.controller.web;

import chess.controller.web.dto.game.GameIdResponseDto;
import chess.controller.web.dto.game.GameRequestDto;
import chess.controller.web.dto.history.HistoryResponseDto;
import chess.controller.web.dto.move.MoveRequestDto;
import chess.controller.web.dto.piece.PieceResponseDto;
import chess.controller.web.dto.score.ScoreResponseDto;
import chess.controller.web.dto.state.StateResponseDto;
import chess.domain.game.Game;
import chess.domain.movecommand.MoveCommand;
import chess.service.ChessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/game")
public class SpringWebChessRestController {

    private final ChessService chessService;

    public SpringWebChessRestController(ChessService chessService) {
        this.chessService = chessService;
    }

    @PostMapping("")
    public ResponseEntity<GameIdResponseDto> saveGame(@RequestBody GameRequestDto gameRequestDto) {
        Game game = Game.of(gameRequestDto.getRoomName(),
                gameRequestDto.getWhiteUsername(), gameRequestDto.getBlackUsername());
        return ResponseEntity.ok().body(new GameIdResponseDto(chessService.saveGame(game)));
    }

    @GetMapping("/{id}/load")
    public ResponseEntity<List<PieceResponseDto>> findPiecesByGameId(@PathVariable Long id) {
        return ResponseEntity.ok().body(chessService.findPiecesById(id));
    }

    @GetMapping("/{id}/score")
    public ResponseEntity<ScoreResponseDto> findScoreByGameId(@PathVariable Long id) {
        return ResponseEntity.ok().body(chessService.findScoreByGameId(id));
    }

    @GetMapping("/{id}/state")
    public ResponseEntity<StateResponseDto> findStateByGameId(@PathVariable Long id) {
        return ResponseEntity.ok().body(chessService.findStateByGameId(id));
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<HistoryResponseDto>> findHistoryByGameId(@PathVariable Long id) {
        return ResponseEntity.ok().body(chessService.findHistoryByGameId(id));
    }

    @GetMapping("/{id}/path")
    public ResponseEntity<List<String>> movablePath(@PathVariable Long id, @RequestParam String source) {
        return ResponseEntity.ok().body(chessService.movablePath(source, id).getPath());
    }

    @PostMapping("/{id}/move")
    public ResponseEntity<HistoryResponseDto> move(@PathVariable Long id, @RequestBody MoveRequestDto moveRequestDto) {
        MoveCommand moveCommand = new MoveCommand(moveRequestDto.getSource(), moveRequestDto.getTarget());
        return ResponseEntity.ok().body(chessService.move(moveCommand, id));
    }
}
