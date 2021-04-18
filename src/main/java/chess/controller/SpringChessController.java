package chess.controller;

import chess.service.ChessService;
import chess.service.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/game/{roomId}")
    private String loadGame(@PathVariable Long roomId, Model model) {
        chessService.load(roomId, model);
        return "chessboard";
    }

    @GetMapping("/create")
    private String create(@RequestParam("roomName") String roomName, Model model) {
        Long roomId = roomService.create(roomName);
        return "redirect:game/" + roomId;
    }
}
