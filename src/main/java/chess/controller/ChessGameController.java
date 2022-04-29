package chess.controller;

import chess.domain.gameRoom.dto.ChessGameRoomShowInfoDTO;
import chess.service.ChessService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ChessGameController {

    private final ChessService chessService;

    public ChessGameController(final ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String showRooms(Model model) {
        model.addAttribute("games", chessService.getGames());
        return "lobby";
    }

    @PostMapping("/chess/new")
    public String createGame(@RequestParam String gameName, @RequestParam String password) {
        String gameId = chessService.addChessGame(gameName, password);
        return "redirect:/chess/game/" + gameId;
    }

    @GetMapping("/chess/game/{id}")
    public String showChessGameRoom(@PathVariable String id, Model model) {
        ChessGameRoomShowInfoDTO chessGameRoomInfoDTO = chessService.findGameById(id);
        model.addAttribute("chessGameRoom", chessGameRoomInfoDTO);
        return "game";
    }
}
