package wooteco.chess.controller;

import org.springframework.stereotype.Controller;
import wooteco.chess.service.BoardService;

@Controller
public class BoardController {

    private final BoardService boardService;

    public BoardController(final BoardService boardService) {
        this.boardService = boardService;
    }

//    @PutMapping("/room/{room_id}/move")
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<?> move(
//            @PathVariable("room_id") Long roomId,
//            @RequestParam String fromPiece,
//            @RequestParam String toPiece) throws SQLException {
//        try {
//            Board board = boardService.movePiece(roomId, fromPiece, toPiece);
//            if (boardService.isFinish(board)) {
//                return ResponseEntity.ok().body(boardService.receiveWinner());
//            }
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//        return ResponseEntity.ok().body(fromPiece + " " + toPiece);
//    }
//
//    @GetMapping("/room/{room_id}/score")
//    public String status(
//            @PathVariable("room_id") Long roomId,
//            Model model) throws SQLException {
//        model.addAllAttributes(boardService.showScoreStatus(roomId));
//        return "index";
//    }

//    @GetMapping("/exit")
//    public String end(Model model) throws SQLException {
//        // "/" redirect;
//        model.addAllAttributes(boardService.receiveEmptyBoard());
//        return "index";

//    }
}
