package chess.controller;

import chess.service.ChessGameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@Controller
public class ViewController {

    private final ChessGameService chessGameService;

    public ViewController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping("/")
    public String index(HttpSession session) {
        session.removeAttribute("roomId");
        return "index";
    }

    @GetMapping("/game/{roomId}")
    public String moveGameRoom(@PathVariable long roomId, HttpSession session) {
        chessGameService.checkRoomExist(roomId);

        session.setAttribute("roomId", roomId);
        return "game";
    }

    @GetMapping("/game/create")
    public String createGame() {
        return "newGame";
    }

    @GetMapping("/game/list")
    public String moveGameList(Model model) {
        model.addAttribute("list", chessGameService.loadRoomList());
        return "gameList";
    }
}
