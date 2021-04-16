package chess.controller;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import chess.controller.dto.PieceDto;
import chess.controller.dto.PositionDto;
import chess.controller.dto.RoundStatusDto;
import chess.domain.ChessGame;
import chess.domain.Position;
import chess.domain.converter.StringPositionConverter;
import chess.domain.piece.Piece;
import chess.exception.ChessException;
import chess.service.ChessService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("http")
public class ChessApiController {

    private final ChessService chessService;
    private final StringPositionConverter stringPositionConverter;

    public ChessApiController(ChessService chessService) {
        this.chessService = chessService;
        this.stringPositionConverter = new StringPositionConverter();
    }

    @GetMapping("/{gameId}/pieces")
    public ResponseEntity<List<PieceDto>> pieces(@PathVariable Long gameId) {
        return ResponseEntity.ok(chessService.loadChess(gameId)
            .pieces().asList().stream()
            .map(PieceDto::new)
            .collect(Collectors.toList()));
    }

    @GetMapping("/{gameId}/roundstatus")
    public ResponseEntity<RoundStatusDto> roundStatus(@PathVariable Long gameId) {
        final ChessGame chessGame = chessService.loadChess(gameId);
        return ResponseEntity.ok(new RoundStatusDto(
            mapMovablePositions(chessGame.currentColorPieces()),
            chessGame.currentColor(),
            chessGame.gameResult(),
            chessGame.isChecked(),
            chessGame.isKingDead()
        ));
    }

    private Map<String, List<String>> mapMovablePositions(List<Piece> pieces) {
        return pieces.stream()
            .collect(toMap(
                piece -> piece.currentPosition().columnAndRow(),
                piece -> piece.movablePositions()
                    .stream()
                    .map(Position::columnAndRow)
                    .collect(toList())
            ));
    }

    @PostMapping("/{gameId}/move")
    public ResponseEntity<RoundStatusDto> move(@PathVariable Long gameId, @RequestBody PositionDto position) {
        chessService.move(
            gameId,
            stringPositionConverter.convert(position.getCurrentPosition()),
            stringPositionConverter.convert(position.getTargetPosition())
        );
        return roundStatus(gameId);
    }

    @GetMapping("/{gameId}/restart")
    public void restart(@PathVariable Long gameId) {
        chessService.exitGame(gameId);
    }

    @DeleteMapping("/exit/{gameId}")
    public void exit(@PathVariable Long gameId) {
        chessService.exitGame(gameId);
    }

    @PutMapping("/save/{gameId}")
    public void save(@PathVariable Long gameId) {
        chessService.saveGame(gameId);
    }

    @ExceptionHandler(ChessException.class)
    public ResponseEntity<String> chessException(Exception exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
