package chess.controller.web;

import chess.controller.web.dto.GameIdDto;
import chess.controller.web.dto.game.GameRequestDto;
import chess.controller.web.dto.history.HistoryResponseDto;
import chess.controller.web.dto.move.MoveRequestDto;
import chess.controller.web.dto.piece.PieceResponseDto;
import chess.controller.web.dto.score.ScoreResponseDto;
import chess.controller.web.dto.state.StateResponseDto;
import chess.service.ChessService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SpringWebChessRestController {

    private final ChessService chessService;

    public SpringWebChessRestController(ChessService chessService) {
        this.chessService = chessService;
    }

    @PostMapping("/game")
    public GameIdDto saveGame(@RequestBody GameRequestDto gameRequestDto) {
        return new GameIdDto(chessService.saveGame(gameRequestDto.toGame()));
    }

    @GetMapping("/game/{id}/load")
    public List<PieceResponseDto> findPiecesByGameId(@PathVariable Long id) {
        return chessService.findPiecesById(id);
    }

    @GetMapping("/game/{id}/score")
    public ScoreResponseDto findScoreByGameId(@PathVariable Long id) {
        return chessService.findScoreByGameId(id);
    }

    @GetMapping("/game/{id}/state")
    public StateResponseDto findStateByGameId(@PathVariable Long id) {
        return chessService.findStateByGameId(id);
    }

    @GetMapping("/game/{id}/history")
    public List<HistoryResponseDto> findHistoryByGameId(@PathVariable Long id) {
        return chessService.findHistoryByGameId(id);
    }

    @GetMapping("/game/{id}/path")
    public List<String> movablePath(@PathVariable Long id, @RequestParam String source) {
        return chessService.movablePath(source, id).getPath();
    }

    @PostMapping("/game/{id}/move")
    public HistoryResponseDto move(@PathVariable Long id, @RequestBody MoveRequestDto moveRequestDto) {
        return chessService.move(moveRequestDto.toMoveCommand(), id);
    }
}
