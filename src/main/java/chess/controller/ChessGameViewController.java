package chess.controller;

import chess.dto.request.RoomRequest;
import chess.service.ChessService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@Controller
public class ChessGameViewController {

    private final ChessService chessService;

    public ChessGameViewController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String returnHomeView() {
        return "index";
    }

    @GetMapping("/game")
    public String returnRoomView() {
        return "game";
    }

    @GetMapping("/game/list")
    public String returnGameListView() {
        return "gamelist";
    }

    @GetMapping("/board/{id}")
    public String returnBoardView(@PathVariable Long id) throws SQLException {
        chessService.validateGameId(id);
        return "board";
    }

    @PostMapping("/initialize/board")
    public String createRoom(@ModelAttribute RoomRequest roomRequest) {
        Long id = chessService.initializeGame(roomRequest);
        return "redirect:/board/" + id;
    }

    @PostMapping("/participate/{id}")
    public String participateRoom(@PathVariable Long id) throws SQLException {
        chessService.validateGameId(id);
        chessService.loadExistGame(id);
        return "redirect:/board/" + id;
    }
}
