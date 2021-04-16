package chess.controller;

import chess.dto.ChessBoardDTO;
import chess.dto.FinishDTO;
import chess.dto.MoveDTO;
import chess.dto.TurnDTO;
import chess.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class GameController {
    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/{gameId}")
    public String initBoard() {
        return "index";
    }

    @GetMapping("/chessboard/{gameId}")
    @ResponseBody
    public ChessBoardDTO loadGame(@PathVariable String gameId) {
        return gameService.loadGame(gameId);
    }

    //    gameid를 int로 받을지 string으로 받을지
    @GetMapping("/turn/{gameId}")
    @ResponseBody
    public TurnDTO turn(@PathVariable String gameId) {
        return gameService.turn(gameId);
    }

    @PutMapping(path = "/move/{gameId}")
    public ResponseEntity move(@PathVariable String gameId, @RequestBody MoveDTO moveDTO) {
        return gameService.move(gameId, moveDTO);
    }
    //    @GetMapping
//    public String {
//        "/:id/result", ChessService::result}
//
    @GetMapping("/finish/{gameId}")
    @ResponseBody
    public FinishDTO isFinished(@PathVariable String gameId) {
        return gameService.isFinished(gameId);
    }

    @PostMapping("/finish/{gameId}")
    public ResponseEntity finish(@PathVariable String gameId) {
        gameService.finish(gameId);
        return new ResponseEntity(HttpStatus.OK);
    }
}
