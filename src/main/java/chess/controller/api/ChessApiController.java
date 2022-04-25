package chess.controller.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

import chess.domain.board.ChessBoard;
import chess.domain.board.factory.BoardFactory;
import chess.domain.board.position.Position;
import chess.domain.piece.Piece;
import chess.dto.request.web.SaveRequest;
import chess.dto.response.web.BoardResponse;
import chess.dto.response.web.LastGameResponse;
import chess.service.ChessService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chess")
public class ChessApiController {

    @Autowired
    private ChessBoard chessBoard;

    @Autowired
    private BoardFactory boardFactory;

    @Autowired
    private ChessService chessService;

    @GetMapping(value = "/board", produces = APPLICATION_JSON_VALUE)
    public BoardResponse board() {
        return BoardResponse.from(boardFactory.create());
    }

    @GetMapping(value = "/current-team", produces = TEXT_HTML_VALUE)
    public String currentTeam() {
        return chessBoard.currentState().getName();
    }

    @PatchMapping(value = "/move", produces = APPLICATION_JSON_VALUE)
    public BoardResponse move(@RequestParam("from") String fromString, @RequestParam("to") String toString) {
        Position from = Position.of(fromString);
        Position to = Position.of(toString);
        chessBoard.movePiece(from, to);

        Map<Position, Piece> movedBoard = chessBoard.getBoard();
        return BoardResponse.from(movedBoard);
    }

    @PutMapping(value = "/save-game", consumes = APPLICATION_JSON_VALUE)
    public void saveGame(@RequestBody SaveRequest saveRequest) {
        chessService.saveGame(saveRequest);
    }

    @GetMapping(value = "/load-last-game", produces = APPLICATION_JSON_VALUE)
    public LastGameResponse loadLastGame() {
        LastGameResponse lastGameResponse = chessService.loadLastGame();
        return lastGameResponse;
    }

    @PutMapping("/test-put")
    public void testPUt() {
        System.out.println("ChessApiController.postTest");
    }
}
