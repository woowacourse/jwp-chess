package chess;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ChessController {

    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String index() {
        return "roby.html";
    }

    @GetMapping("/room")
    public String room(@RequestParam String name,
                       Model model) {
        chessService.createRoom(name);
        model.addAttribute("name", name);
        return "room.html";
    }
}
