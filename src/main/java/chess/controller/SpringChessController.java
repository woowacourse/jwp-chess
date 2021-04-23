package chess.controller;

import chess.service.ChessService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public final class SpringChessController {

    private final ChessService chessService;

    public SpringChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String lobby(Model model) {
        model.addAttribute("rooms", chessService.openedRooms());
        return "lobby";
    }

    @GetMapping("/rooms/{id}")
    public String joinRoom(@PathVariable String id, Model model) {
        model.addAttribute("board", chessService.latestBoard(id));
        model.addAttribute("roomId", id);
        model.addAttribute("userInfo", chessService.usersInRoom(id));
        return "index";
    }
}
