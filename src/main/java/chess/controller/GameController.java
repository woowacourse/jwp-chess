package chess.controller;

import chess.dto.ChessBoardDTO;
import chess.dto.MoveDTO;
import chess.dto.TurnDTO;
import chess.service.ChessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class GameController {
    private final ChessService chessService;

    @Autowired
    public GameController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/{gameId}")
    public String initBoard() {
        return "index";
    }

    @GetMapping("/chessboard/{gameId}")
    @ResponseBody
    public ChessBoardDTO loadGame(@PathVariable String gameId) {
        return chessService.loadGame(gameId);
    }

    //    gameid를 int로 받을지 string으로 받을지
    @GetMapping("/turn/{gameId}")
    @ResponseBody
    public TurnDTO turn(@PathVariable String gameId) {
        return chessService.turn(gameId);
    }

    @PutMapping(path = "/move/{gameId}")
    public ResponseEntity move(@PathVariable String gameId, @RequestBody MoveDTO moveDTO) {
        return chessService.move(gameId, moveDTO);
    }
//    @GetMapping
//    public String {
//        "/:id/result", ChessService::result}
//
//    @GetMapping
//    public String {
//        "/:id/finish", ChessService::finished}
//
//
//    post("/:id/finish", ChessService::finish);
}
