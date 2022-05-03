package chess.controller;

import chess.service.ChessService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ChessWebController {

    private final ChessService chessService;

    public ChessWebController(final ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String index() {
        return "lobby";
    }

    @GetMapping("/room/{roomId}")
    public String room(@PathVariable final Long roomId) {
        chessService.checkRoomExist(roomId);
        return "room";
    }
}
