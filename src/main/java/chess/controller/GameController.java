package chess.controller;

import chess.controller.request.MoveRequest;
import chess.controller.response.ChessGameResponse;
import chess.controller.response.ScoresResponse;
import chess.domain.Score;
import chess.domain.piece.PieceColor;
import chess.serviece.dto.GameDto;
import chess.serviece.dto.PieceDto;
import chess.serviece.ChessGameService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/games")
public class GameController {

    private final ChessGameService chessGameService;

    public GameController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping
    public String init() {
        return "game";
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChessGameResponse> load(@PathVariable Long id) {
        GameDto game = chessGameService.getGame(id);
        List<PieceDto> pieces = chessGameService.getPiecesOfGame(id);
        return ResponseEntity.ok().body(ChessGameResponse.from(game, pieces));
    }

    @GetMapping("/{id}/score")
    public ResponseEntity<ScoresResponse> score(@PathVariable Long id) {
        Map<PieceColor, Score> scoreByColor = chessGameService.getScore(id);
        return ResponseEntity.ok().body(ScoresResponse.of(scoreByColor));
    }

    @PutMapping(value = "/{id}/pieces")
    public ResponseEntity<Void> move(@PathVariable Long id, @RequestBody MoveRequest moveRequest) {
        chessGameService.movePiece(id, moveRequest.toEntity());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScoresResponse> end(@PathVariable Long id) {
        Map<PieceColor, Score> scoresByColor = chessGameService.finishGame(id);
        return ResponseEntity.ok().body(ScoresResponse.of(scoresByColor));
    }
}
