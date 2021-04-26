package chess.controller.web;

import chess.chessgame.domain.room.game.ChessGameManager;
import chess.service.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/room")
@Controller
public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("{roomId:[\\d]+}")
    public String enterRoom(@PathVariable long roomId, Model model) {
        ChessGameManager game = roomService.findGameBy(roomId);
        model.addAttribute("gameId", game.getId());
        return "chess";
    }
}
