package chess.api;

import chess.domain.piece.Position;
import chess.service.ChessGameService;
import chess.view.dto.ChessGameDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpringChessApiController {

    private final ChessGameService chessGameService;

    public SpringChessApiController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping("/pieces")
    public ChessGameDto findPieces(@RequestParam(value = "source") String sourcePosition,
                                   @RequestParam(value = "target") String targetPosition) {
        Position source = Position.parseChessPosition(sourcePosition);
        Position target = Position.parseChessPosition(targetPosition);
        return chessGameService.moveChessPiece(source, target);
    }

}
