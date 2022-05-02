package chess.controller;

import chess.service.ChessService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ChessViewController {

    private final ChessService chessService;

    public ChessViewController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String getRooms(Model model) {
        model.addAttribute("rooms", chessService.getRooms());
        return "index";
    }

    @GetMapping("/room/{roomId}")
    public String showRoom(@PathVariable("roomId") int roomId, Model model) {
        model.addAttribute("roomId", roomId);
        return "board";
    }
}
