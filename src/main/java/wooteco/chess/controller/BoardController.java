package wooteco.chess.controller;

import com.google.gson.Gson;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import wooteco.chess.domain.result.Status;
import wooteco.chess.service.BoardService;

@RestController
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
    public String status(
            @PathVariable("room_id") Long roomId,
            ModelAndView model) {
        model.addObject(boardService.showScoreStatus(roomId));
        return new Gson().toJson(boardService.showScoreStatus(roomId));
    }
}
