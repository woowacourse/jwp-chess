package chess.controller.api;

import chess.domain.board.ChessBoard;
import chess.domain.board.factory.BoardFactory;
import chess.domain.board.position.Position;
import chess.domain.piece.Piece;
import chess.dto.request.web.SaveRequest;
import chess.dto.response.web.BoardResponse;
import chess.service.ChessService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChessApiController {

    @Autowired
    private ChessBoard chessBoard;

    @Autowired
    private BoardFactory boardFactory;

    @Autowired
    private ChessService chessService;

    @GetMapping(value = "/board", produces = MediaType.APPLICATION_JSON_VALUE)
    public BoardResponse board() {
        return BoardResponse.from(boardFactory.create());
    }

    @GetMapping(value = "/current-team", produces = MediaType.TEXT_HTML_VALUE)
    public String currentTeam() {
        return chessBoard.currentState().getName();
    }

    @PostMapping(value = "/move", produces = MediaType.APPLICATION_JSON_VALUE)
    public BoardResponse move(@RequestParam("from") String fromString, @RequestParam("to") String toString) {
        Position from = Position.of(fromString);
        Position to = Position.of(toString);
        chessBoard.movePiece(from, to);

        Map<Position, Piece> movedBoard = chessBoard.getBoard();
        return BoardResponse.from(movedBoard);
    }

    @PostMapping(value = "save-game", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void saveGame(@RequestBody SaveRequest saveRequest) {
        chessService.saveGame(saveRequest);
    }
}
