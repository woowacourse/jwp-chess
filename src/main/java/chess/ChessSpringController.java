package chess;

import chess.service.ChessService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ChessSpringController {

    @GetMapping("/")
    public String index() {
        return "roby";
    }

    @GetMapping("/room")
    public String room(@RequestParam(value = "name") String name,
                       Model model) {
        ChessService chessService = new ChessService();
        chessService.createRoom(name);
        model.addAttribute("name", name);
        return "room";
    }
}
