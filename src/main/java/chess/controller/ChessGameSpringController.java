package chess.controller;

import javax.validation.Valid;
import chess.domain.ChessGameService;
import chess.domain.board.strategy.WebBasicBoardStrategy;
import chess.dto.GameStatusDto;
import chess.dto.MoveDto;
import chess.dto.ScoreDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/rooms/game")
public class ChessGameSpringController {

    private final ChessGameService chessGameService;

    public ChessGameSpringController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @ResponseBody
    @GetMapping("/{id}")
    public ResponseEntity<GameStatusDto> load(@PathVariable int id) {
        GameStatusDto status = chessGameService.loadChessGame(id);
        return ResponseEntity.ok().body(status);
    }

    @ResponseBody
    @GetMapping("/start/{id}")
    public ResponseEntity<GameStatusDto> start(@PathVariable int id) {
        GameStatusDto status = chessGameService.startChessGame(new WebBasicBoardStrategy(), id);
        return ResponseEntity.ok().body(status);
    }

    @ResponseBody
    @PutMapping("/move")
    public ResponseEntity<GameStatusDto> move(@RequestBody @Valid MoveDto moveDto) {
        GameStatusDto status = chessGameService.move(moveDto.getFrom(), moveDto.getTo(), moveDto.getRoomId());
        return ResponseEntity.ok().body(status);
    }

    @ResponseBody
    @GetMapping("/status/{id}")
    public ResponseEntity<ScoreDto> status(@PathVariable int id) {
        ScoreDto score = chessGameService.createScore(id);
        return ResponseEntity.ok().body(score);
    }

    @ResponseBody
    @PostMapping("/end/{id}")
    public ResponseEntity<ScoreDto> end(@PathVariable int id) {
        ScoreDto score = chessGameService.end(id);
        return ResponseEntity.ok().body(score);
    }
}
