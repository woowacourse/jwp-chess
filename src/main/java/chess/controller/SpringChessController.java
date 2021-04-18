package chess.controller;

import chess.exception.DataAccessException;
import chess.service.ChessService;
import chess.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class SpringChessController {
    private final ChessService chessService;
    private final RoomService roomService;

    public SpringChessController(ChessService chessService, RoomService roomService) {
        this.chessService = chessService;
        this.roomService = roomService;
    }

    @GetMapping("/")
    private String mainPage(Model model) {
        model.addAttribute("roomList", roomService.load());
        return "index";
    }

    @GetMapping("/game/{roomId}")
    private String loadGame(@PathVariable Long roomId, Model model) {
        chessService.load(roomId, model);
        return "chessboard";
    }

    @GetMapping("/create")
    private String createRoom(@RequestParam("roomName") String roomName) {
        Long roomId = roomService.create(roomName);
        return "redirect:game/" + roomId;
    }

    @GetMapping("/delete/{roomId}")
    private String deleteRoom(@PathVariable("roomId") Long roomId) {
        roomService.delete(roomId);
        return "redirect:/";
    }

    @PostMapping("/game/{roomId}/move")
    private String move(@PathVariable("roomId") Long roomId,
                        @RequestParam("from") String from,
                        @RequestParam("to") String to) {
        chessService.move(roomId, from, to);
        return "redirect:/game/" + roomId;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handle(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handle(DataAccessException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
