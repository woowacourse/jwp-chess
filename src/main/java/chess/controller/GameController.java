package chess.controller;

import chess.dto.*;
import chess.utils.Serializer;
import chess.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return Serializer.deserializeAsDTO(gameService.loadGame(gameId));
    }

    //    gameid를 int로 받을지 string으로 받을지
    @GetMapping("/turn/{gameId}")
    @ResponseBody
    public TurnDTO turn(@PathVariable String gameId) {
        return new TurnDTO(gameService.turn(gameId));
    }

    //여기서 DTO요리
    @PutMapping(path = "/move/{gameId}")
    public ResponseEntity move(@PathVariable String gameId, @RequestBody MoveDTO moveDTO) {
        try {
            gameService.move(gameId, moveDTO);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/finishById/{gameId}")
    @ResponseBody
    public FinishDTO isFinished(@PathVariable String gameId) {
        return new FinishDTO(gameService.isFinished(gameId));
    }

    @PostMapping("/finish/{gameId}")
    public ResponseEntity finish(@PathVariable String gameId) {
        gameService.finish(gameId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/scoreById/{gameId}")
    @ResponseBody
    public ScoreDTO score(@PathVariable String gameId) {
        List<Double> scores = gameService.score(gameId);
        return new ScoreDTO(scores.get(0), scores.get(1));
    }

    @PostMapping("/restart/{gameId}")
    @ResponseBody
    public ChessBoardDTO restart(@PathVariable String gameId) {
        return Serializer.deserializeAsDTO(gameService.restart(gameId));
    }
}
