package chess.controller;

import chess.domain.ChessGame;
import chess.domain.dto.*;
import chess.service.ChessService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class ChessController {
    private final ChessService chessService;
    private final ChessGame chessGame;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
        this.chessGame = new ChessGame();
    }

    @GetMapping("/")
    public String start() {
        return "main.html";
    }

    @GetMapping("/room/{roomNumber}")
    public String chessView() {
        return "/chess.html";
    }

    @GetMapping("/room")
    @ResponseBody
    public RoomsDto showRoomList() {
        return chessService.getRoomList();
    }

    @GetMapping("/board/{roomNumber}")
    @ResponseBody
    public BoardDto loadSavedBoard(@PathVariable int roomNumber) {
        return chessService.getSavedBoardInfo(chessGame, roomNumber);
    }

    @PutMapping("/board/{roomNumber}")
    @ResponseBody
    public BoardDto resetBoard(@PathVariable int roomNumber) {
        return chessService.initiateBoard(chessGame, roomNumber);
    }

    @GetMapping("/score")
    @ResponseBody
    public ScoreDto scoreStatus() {
        return chessGame.scoreStatus();
    }

    @PostMapping("/move/{roomNumber}")
    @ResponseBody
    public ResponseDto move(@RequestBody MoveInfoDto moveInfoDto, @PathVariable int roomNumber) {
        return chessService.move(chessGame, moveInfoDto, roomNumber);
    }

    @GetMapping("/move/{roomNumber}")
    @ResponseBody
    public BoardDto getCurrentBoard(@PathVariable int roomNumber) {
        return chessService.getCurrentBoard(chessGame, roomNumber);
    }
}
