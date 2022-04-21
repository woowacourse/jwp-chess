package chess.controller;

import chess.domain.ChessGameService;
import chess.domain.board.strategy.WebBasicBoardStrategy;
import chess.dto.ErrorDto;
import chess.dto.GameStatusDto;
import chess.dto.MoveDto;
import chess.dto.ScoreDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ChessSpringController {

    private final ChessGameService chessGameService;

    public ChessSpringController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping("/")
    public String enter() {
        chessGameService.init();
        return "index";
    }

    @ResponseBody
    @GetMapping("/start")
    public GameStatusDto start() {
        return chessGameService.startChessGame(new WebBasicBoardStrategy());
    }

    @ResponseBody
    @PostMapping("/move")
    public ResponseEntity move(@RequestBody MoveDto moveDto) {
        GameStatusDto gameStatusDto = null;
        try {
            gameStatusDto = chessGameService.move(moveDto.getFrom(), moveDto.getTo());
        } catch (Exception e) {
            return ResponseEntity.ok().body(new ErrorDto(e.getMessage()));
        }
        return ResponseEntity.ok().body(gameStatusDto);
    }

    @ResponseBody
    @GetMapping("/status")
    public ResponseEntity status() {
        ScoreDto scoreDto = null;
        try {
            scoreDto = chessGameService.createScore();
        } catch (Exception e) {
            return ResponseEntity.ok().body(new ErrorDto(e.getMessage()));
        }
        return ResponseEntity.ok().body(scoreDto);
    }
    
    @ResponseBody
    @GetMapping("/end")
    public ResponseEntity end() {
        ScoreDto scoreDto = null;
        try {
            scoreDto = chessGameService.end();
        } catch (Exception e) {
            return ResponseEntity.ok().body(new ErrorDto(e.getMessage()));
        }
        return ResponseEntity.ok().body(scoreDto);
    }
}
