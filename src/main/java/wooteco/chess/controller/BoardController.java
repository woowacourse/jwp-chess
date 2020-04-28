package wooteco.chess.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wooteco.chess.domain.board.Board;
import wooteco.chess.service.BoardService;

import java.sql.SQLException;

@Controller
public class BoardController {

    private final BoardService boardService;

    public BoardController(final BoardService boardService) {
        this.boardService = boardService;
    }

    @PutMapping("/room/{room_id}/move")
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> move(
            @PathVariable("room_id") Long roomId,
            @RequestParam String fromPiece,
            @RequestParam String toPiece) throws SQLException {
        try {
            Board board = boardService.movePiece(roomId, fromPiece, toPiece);
            if (boardService.isFinish(board)) {
                return ResponseEntity.ok().body(boardService.receiveWinner(roomId));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body(fromPiece + " " + toPiece);
    }

//
//    @GetMapping("/room/{room_id}/score")
//    public String status(
//            @PathVariable("room_id") Long roomId,
//            Model model) throws SQLException {
//        model.addAllAttributes(boardService.showScoreStatus(roomId));
//        return "index";
//    }

//    @GetMapping("/turn")
//    @ResponseBody
//    public String Status() throws SQLException {
//        Team currentTurn = boardService.getCurrentTurn();
//        return currentTurn.name();
//    }
}
