package chess.controller;

import chess.dto.RoomResponseDto;
import chess.service.GameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/games")
@Controller
public class GameViewController {

    final GameService gameService;

    public GameViewController(chess.service.GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/{id}")
    public String getGameById() {
        return "chess";
    }

    @GetMapping("/room-list")
    public String rooms(final Model model) {
        List<RoomResponseDto> rooms = gameService.getRooms();
        model.addAttribute("rooms", rooms);
        return "room-list";
    }
}
