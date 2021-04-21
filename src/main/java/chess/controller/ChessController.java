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

    @GetMapping("/room")
    @ResponseBody
    public RoomsDto showRoomList() {
        return chessService.getRoomList();
    }

    @GetMapping("/board")
    @ResponseBody
    public BoardDto loadSavedBoard() {
        return chessService.getSavedBoardInfo(chessGame);
    }

    @PutMapping("/board")
    @ResponseBody
    public BoardDto resetBoard() {
        return chessService.initiateBoard(chessGame);
    }

    @GetMapping("/score")
    @ResponseBody
    public ScoreDto scoreStatus() {
        return chessGame.scoreStatus();
    }

    @PostMapping("/move")
    @ResponseBody
    public ResponseDto move(@RequestBody MoveInfoDto moveInfoDto) {
        return chessService.move(chessGame, moveInfoDto);
    }

    @GetMapping("/move")
    @ResponseBody
    public BoardDto getCurrentBoard() {
        return chessService.getCurrentBoard(chessGame);
    }
}
