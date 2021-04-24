package chess.controller;

import chess.service.ChessService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class SpringChessController {

    private final ChessService chessService;

    public SpringChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("names", chessService.roomNames());
        return "index";
    }

    @PostMapping("/white-game")
    public String whiteStart(Model model, HttpServletRequest request, HttpSession session) {
        Map<String, String[]> paramMap = request.getParameterMap();
        String room = paramMap.get("roomName")[0];
        String password = paramMap.get("wPassword")[0];

        model.addAttribute("room", room);
        session.setAttribute("password", password);

        return "game";
    }

    @PostMapping("/black-game")
    public String blackStart(Model model, HttpServletRequest request, HttpSession session) {
        Map<String, String[]> paramMap = request.getParameterMap();
        String room = paramMap.get("roomName")[0];
        String password = paramMap.get("bPassword")[0];

        chessService.deleteRoomFromList(room);

        model.addAttribute("room", room);
        session.setAttribute("password", password);

        return "game";
    }
}
