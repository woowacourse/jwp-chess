package chess.web.controller;

import chess.web.service.ChessService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ChessViewController {

    private final ChessService chessService;

    public ChessViewController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String getRoomList(Model model) {
        List<Long> roomListId = chessService.getRoomList();
        model.addAttribute("list", roomListId);
        return "index";
    }

}
