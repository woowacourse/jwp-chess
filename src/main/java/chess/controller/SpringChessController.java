package chess.controller;

import chess.service.ChessService;
import chess.service.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}
