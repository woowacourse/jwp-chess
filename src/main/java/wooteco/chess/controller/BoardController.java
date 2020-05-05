package wooteco.chess.controller;

import com.google.gson.Gson;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wooteco.chess.domain.result.Status;
import wooteco.chess.service.BoardService;

@Controller
public class BoardController {

    private final BoardService boardService;

    public BoardController(final BoardService boardService) {
        this.boardService = boardService;
    }

    @PutMapping("/room/{room_id}/move")
    public ResponseEntity<?> move(
            @PathVariable("room_id") Long roomId,
            @RequestParam String fromPiece,
            @RequestParam String toPiece) {
            Status status = boardService.movePiece(roomId, fromPiece, toPiece);
            if (status.isFinish()) {
                return ResponseEntity.ok().body(boardService.receiveWinner(roomId));
            }
        return ResponseEntity.ok().body(fromPiece + " " + toPiece);
    }

    @GetMapping("/room/{room_id}/score")
    @ResponseBody
    public String status(
            @PathVariable("room_id") Long roomId,
            Model model) {
        model.addAttribute(boardService.showScoreStatus(roomId));
        return new Gson().toJson(boardService.showScoreStatus(roomId));
    }
}
