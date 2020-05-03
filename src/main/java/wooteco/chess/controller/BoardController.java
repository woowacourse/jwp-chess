package wooteco.chess.controller;

import com.google.gson.Gson;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wooteco.chess.domain.board.Board;
import wooteco.chess.service.BoardService;

@RequestMapping("/room")
@Controller
public class BoardController {

    private final BoardService boardService;

    public BoardController(final BoardService boardService) {
        this.boardService = boardService;
    }

    @PutMapping("/{room_id}/move")
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> move(
            @PathVariable("room_id") Long roomId,
            @RequestParam String fromPiece,
            @RequestParam String toPiece) {
        Board board;
        try {
            board = boardService.movePiece(roomId, fromPiece, toPiece);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        if (boardService.isFinish(board)) {
            return ResponseEntity.ok().body(boardService.receiveWinner(roomId));
        }
        return ResponseEntity.ok().body(fromPiece + " " + toPiece);
    }

    @GetMapping("/{room_id}/score")
    @ResponseBody
    public String status(
            @PathVariable("room_id") Long roomId, Model model) {
        model.addAttribute(boardService.showScoreStatus(roomId));
        return new Gson().toJson(boardService.showScoreStatus(roomId));
    }
}
