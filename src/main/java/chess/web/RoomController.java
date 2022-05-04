package chess.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import chess.domain.board.Board;
import chess.domain.board.RegularRuleSetup;
import chess.domain.chessgame.ChessGame;
import chess.service.ChessGameService;

@Controller
@RequestMapping("/rooms")
public class RoomController {
    private final ChessGameService chessGameService;

    public RoomController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @PostMapping
    public String createRoom(@RequestParam String name, @RequestParam String password) {
        ChessGame chessGame = chessGameService.create(name, password);
        return "redirect:/rooms/" + chessGame.getId();
    }

    @GetMapping("/{roomId}")
    public String board(@PathVariable int roomId) {
        chessGameService.getChessGameById(roomId);
        return "/board.html";
    }

    @PostMapping("/{roomId}")
    public String startNewGame(@PathVariable int roomId) {
        chessGameService.startNewGame(roomId);
        return "redirect:/api/rooms/" + roomId;
    }
}
