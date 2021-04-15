package chess.controller;

import chess.dto.RoomIdDTO;
import chess.service.ChessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
    public String chess(@PathVariable int gameId) {
        return "index";
    }

    @GetMapping("/lobby/new")
    @ResponseBody
    public RoomIdDTO newGame(){
//        return "1";
        return chessService.newGame();
    }
//
//    @GetMapping
//    public String {
//        "/chessboard/:id", ChessService::loadGame}
//
//    @GetMapping
//    public String {
//        "/:id/turn", ChessService::turn}
//
//    @GetMapping
//    public String {
//        "/:id/result", ChessService::result}
//
//    @GetMapping
//    public String {
//        "/:id/finish", ChessService::finished}
//
//    put("/:id/move", "application/json", ChessService::move);
//
//    post("/:id/finish", ChessService::finish);
}
