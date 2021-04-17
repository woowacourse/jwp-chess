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
@RequestMapping("/game")
public class SpringWebChessRestController {

    private final ChessService chessService;

    public SpringWebChessRestController(ChessService chessService) {
        this.chessService = chessService;
    }

    @PostMapping("")
    public GameIdDto saveGame(@RequestBody GameRequestDto gameRequestDto) {
        return new GameIdDto(chessService.saveGame(gameRequestDto.toGame()));
    }

    @GetMapping("/{id}/load")
    public List<PieceResponseDto> findPiecesByGameId(@PathVariable Long id) {
        return chessService.findPiecesById(id);
    }

    @GetMapping("/{id}/score")
    public ScoreResponseDto findScoreByGameId(@PathVariable Long id) {
        return chessService.findScoreByGameId(id);
    }

    @GetMapping("/{id}/state")
    public StateResponseDto findStateByGameId(@PathVariable Long id) {
        return chessService.findStateByGameId(id);
    }

    @GetMapping("/{id}/history")
    public List<HistoryResponseDto> findHistoryByGameId(@PathVariable Long id) {
        return chessService.findHistoryByGameId(id);
    }

    @GetMapping("/{id}/path")
    public List<String> movablePath(@PathVariable Long id, @RequestParam String source) {
        return chessService.movablePath(source, id).getPath();
    }

    @PostMapping("/{id}/move")
    public HistoryResponseDto move(@PathVariable Long id, @RequestBody MoveRequestDto moveRequestDto) {
        return chessService.move(moveRequestDto.toMoveCommand(), id);
    }
}
