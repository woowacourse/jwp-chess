package chess.web;

import chess.service.GameService;
import chess.web.dto.BoardDto;
import chess.web.dto.CommendDto;
import chess.web.dto.ResultDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/boards")
public class BoardController {

    private final GameService gameService;

    public BoardController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping(value = "/{boardId}", params = "command=move")
    public ResponseEntity<BoardDto> movePiece(@PathVariable int boardId, @RequestBody CommendDto commendDto) {
        gameService.move(boardId, commendDto);
        return ResponseEntity.ok(gameService.gameStateAndPieces(boardId));
    }

    @GetMapping(value = "/{boardId}", params = "command=result")
    public ResponseEntity<ResultDto> result(@PathVariable int boardId) {
        return ResponseEntity.ok(gameService.gameResult(boardId));
    }

    @GetMapping(value = "/{boardId}", params = "command=end")
    public ResponseEntity<ResultDto> end(@PathVariable int boardId) {
        return ResponseEntity.ok(gameService.gameFinalResult(boardId));
    }
}
