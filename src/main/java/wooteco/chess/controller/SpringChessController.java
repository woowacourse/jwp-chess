package wooteco.chess.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wooteco.chess.domain.board.Board;
import wooteco.chess.service.ChessGameService;

import java.sql.SQLException;

@Controller
public class SpringChessController {

    private ChessGameService chessGameService;

    public SpringChessController(final ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping("/")
    public String index(Model model) throws SQLException {
        model.addAllAttributes(chessGameService.receiveLoadedBoard());
        return "index";
    }

    @PostMapping("/start")
    public String start(Model model) throws SQLException {
        chessGameService.initializeTurn();
        model.addAllAttributes(chessGameService.receiveInitializedBoard());
        return "index";
    }

    @PostMapping("/end")
    public String end(Model model) throws SQLException {
        model.addAllAttributes(chessGameService.receiveEmptyBoard());
        return "index";
    }

    @PostMapping("/load")
    public String load(Model model) throws SQLException {
        model.addAllAttributes(chessGameService.receiveLoadedBoard());
        return "index";
    }

    @RequestMapping("/move")
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> move(@RequestParam String fromPiece,
                                  @RequestParam String toPiece) throws SQLException {
        try {
            Board board = chessGameService.receiveMovedBoard(fromPiece, toPiece);
            if (chessGameService.isFinish(board)) {
                return ResponseEntity.ok().body(chessGameService.receiveWinner());
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body(fromPiece + " " + toPiece);
    }

    @PostMapping("/status")
    public String status(Model model) throws SQLException {
        model.addAllAttributes(chessGameService.receiveScoreStatus());
        return "index";
    }

    @GetMapping("/turn")
    @ResponseBody
    public String Status() throws SQLException {
        return chessGameService.getCurrentTurn().toString();
    }
}
