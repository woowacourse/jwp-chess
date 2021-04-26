package chess.controller.web;

import chess.controller.web.dto.*;
import chess.domain.manager.ChessGameManager;
import chess.domain.manager.ChessGameManagerBundle;
import chess.service.ChessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class ChessController {
    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/games")
    public ResponseEntity<RunningGameResponseDto> getGames() {
        ChessGameManagerBundle runningGames = chessService.findRunningGames();
        return ResponseEntity.ok(new RunningGameResponseDto(runningGames.getIdAndNextTurn()));
    }

    @PostMapping("/game/start")
    public ResponseEntity<ChessGameResponseDto> gameStart(@RequestBody Map<String, String> param) {
        String title = param.get("title");
        return ResponseEntity.ok(new ChessGameResponseDto(chessService.start(title)));
    }

    @GetMapping("/games/{id:[\\d]+}/score")
    public ResponseEntity<ScoreResponseDto> getScore(@PathVariable long id) {
        return ResponseEntity.ok(new ScoreResponseDto(chessService.getStatistics(id)));
    }

    @GetMapping("/games/{id:[\\d]+}/load")
    public ResponseEntity<ChessGameResponseDto> loadGame(@PathVariable long id) {
        ChessGameManager load = chessService.findById(id);
        return ResponseEntity.ok(new ChessGameResponseDto(load));
    }

    @GetMapping("/games/{id:[\\d]+}/reset")
    public ResponseEntity<ChessGameResponseDto> resetGame(@PathVariable long id){
        ChessGameManager resetGame = chessService.reset(id);
        return ResponseEntity.ok(new ChessGameResponseDto(resetGame));
    }


    @PostMapping("/games/{id:[\\d]+}/move")
    public ResponseEntity<MoveResponseDto> movePiece(@PathVariable long id, @RequestBody MoveRequestDto moveMessage) {
        chessService.move(id, moveMessage);
        return ResponseEntity.ok(new MoveResponseDto(chessService.isEnd(id), chessService.nextColor(id)));
    }
}
