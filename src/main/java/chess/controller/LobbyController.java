package chess.controller;

import chess.dto.RoomIdDTO;
import chess.service.LobbyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LobbyController {
    private final LobbyService lobbyService;

    @Autowired
    public LobbyController(LobbyService lobbyService) {
        this.lobbyService = lobbyService;
    }

    @GetMapping("/")
    public String lobby() {
        return "lobby";
    }

    @GetMapping("/lobby/new")
    @ResponseBody
    public RoomIdDTO newGame() {
        return lobbyService.newGame();
    }
}
