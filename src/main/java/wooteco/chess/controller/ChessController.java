package wooteco.chess.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import wooteco.chess.service.ChessService;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ChessController {

    private static final String INDEX_PAGE = "index";
    private static final String GAME_PAGE = "game";

    @Autowired
    ChessService chessService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAllAttributes(chessService.loadRooms());
        return INDEX_PAGE;
    }

    @PostMapping("/new-room")
    public String newRoom(HttpServletRequest request, Model model) {
        String roomName = request.getParameter("roomName");
        model.addAllAttributes(chessService.newGame(roomName));
        return "redirect:/";
    }

    @PostMapping("/new-game")
    public String newGame(HttpServletRequest request, Model model) {
        String roomName = request.getParameter("roomName");

        model.addAllAttributes(chessService.newGame(roomName));
        return GAME_PAGE;
    }

    @PostMapping("/load-game")
    public String loadGame(HttpServletRequest request, Model model) {
        String roomName = request.getParameter("roomName");

        model.addAllAttributes(chessService.loadGame(roomName));
        return GAME_PAGE;
    }

    @PostMapping("/move")
    public String move(HttpServletRequest request, Model model) {
        String roomName = request.getParameter("roomName");
        String start = request.getParameter("start");
        String end = request.getParameter("end");

        model.addAllAttributes(chessService.move(roomName, start, end));
        return GAME_PAGE;
    }

    @PostMapping("/show-movable")
    public String showMovable(HttpServletRequest request, Model model) {
        String roomName = request.getParameter("roomName");
        String startPosition = request.getParameter("start");

        model.addAllAttributes(chessService.loadMovable(roomName, startPosition));
        return GAME_PAGE;
    }
}