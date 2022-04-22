package chess.controller;

import chess.dto.MoveDto;
import chess.dto.ResultDto;
import chess.dto.ScoreDto;
import chess.service.ChessService;
import chess.view.ChessMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpringChessGameController {

    private final ChessService chessService;

    public SpringChessGameController() {
        this.chessService = new ChessService();
    }

    @GetMapping("/start")
    public ResponseEntity<ChessMap> start() {
        return ResponseEntity.ok(chessService.initializeGame());
    }

    @GetMapping("/load")
    public ResponseEntity<ChessMap> load() {
        return ResponseEntity.ok(chessService.load());
    }

    @GetMapping("/status")
    public ResponseEntity<ScoreDto> status() {
        return ResponseEntity.ok(chessService.getStatus());
    }

    @PostMapping("/move")
    public ResponseEntity<ChessMap> move(@RequestBody MoveDto moveDto) {
        System.out.println(moveDto);
        return ResponseEntity.ok(chessService.move(moveDto));
    }

    @GetMapping("/end")
    public ResponseEntity<ResultDto> end(){
        final ResultDto resultDto = chessService.getResult();
        chessService.initializeGame();
        return ResponseEntity.ok(resultDto);
    }
}
