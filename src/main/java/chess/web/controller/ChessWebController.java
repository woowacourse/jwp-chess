package chess.web.controller;

import chess.board.Board;
import chess.web.service.ChessService;
import chess.web.service.dto.BoardDto;
import chess.web.service.dto.MoveDto;
import chess.web.service.dto.ScoreDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ChessWebController {

    private final ChessService chessService;

    public ChessWebController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/api/load")
    @ResponseBody
    public ResponseEntity<BoardDto> loadGame() {
        Board board = chessService.loadGame(1L);
        return ResponseEntity.ok().body(BoardDto.from(board));
    }

    @GetMapping("api/restart")
    @ResponseBody
    public ResponseEntity<BoardDto> initBoard() {
        Board board = chessService.initBoard(1L);
        return ResponseEntity.ok().body(BoardDto.from(board));
    }

    @GetMapping("api/status")
    @ResponseBody
    public ResponseEntity<ScoreDto> getStatus() {
        ScoreDto status = chessService.getStatus(1L);
        return ResponseEntity.ok().body(status);
    }

    @PostMapping(value = "/api/move", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<BoardDto> move(@RequestBody MoveDto moveDto) {
        Board board = chessService.move(moveDto, 1L);
        return ResponseEntity.ok().body(BoardDto.from(board));
    }
}
