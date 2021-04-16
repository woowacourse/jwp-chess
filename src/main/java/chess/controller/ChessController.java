package chess.controller;

import chess.dto.*;
import chess.service.ChessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class ChessController {
    private final ChessService chessService;

    @Autowired
    public ChessController(ChessService chessService){
        this.chessService = chessService;
    }
//
    @GetMapping("/")
    public String lobby() {
        return "lobby";
    }

    @GetMapping("/{gameId}")
    public String chess() {
        return "index";
    }

    @GetMapping("/lobby/new")
    @ResponseBody
    public RoomIdDTO newGame(){
        return chessService.newGame();
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

    @PutMapping(path = "/move/{gameId}", consumes = "application/json")
    public ResponseEntity move(@PathVariable String gameId, @RequestBody MoveDTO moveDTO){
        System.out.println("@@@" + "shit");
        System.out.println("@@@" + moveDTO.getSource());
        return chessService.move(gameId, moveDTO);
//        return null;
    }
//
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
