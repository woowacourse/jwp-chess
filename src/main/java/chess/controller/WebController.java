package chess.controller;

import chess.dto.GameRoomDto;
import chess.service.ChessService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class WebController {

    private final ChessService chessService;

    public WebController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String indexPage(Model model) {
        model.addAttribute("games", chessService.findAllGame());
        return "index";
    }

    @GetMapping("/games/{gameId}")
    public String getGame(@PathVariable int gameId, Model model) {
        model.addAttribute("gameId", gameId);
        return "board";
    }

    @PostMapping(value = "/games", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String createGame(@ModelAttribute GameRoomDto gameRoomDto) {
        var gameId = chessService.createRoom(gameRoomDto);
        return "redirect:/games/" + gameId;
    }
}
