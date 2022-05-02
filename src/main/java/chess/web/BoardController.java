package chess.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import chess.service.ChessGameService;
import chess.web.dto.BoardDto;
import chess.web.dto.CommendDto;
import chess.web.dto.ResultDto;

@RestController
@RequestMapping("/boards")
public class BoardController {

    private final ChessGameService chessGameService;

    public BoardController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<BoardDto> movePiece(@PathVariable int boardId, @RequestBody CommendDto commendDto) {
        chessGameService.move(boardId, commendDto);
        return ResponseEntity.ok(chessGameService.getBoardDtoByBoardId(boardId));
    }

    @GetMapping("/{boardId}/result")
    public ResponseEntity<ResultDto> result(@PathVariable int boardId) {
        return ResponseEntity.ok(chessGameService.getResult(boardId));
    }

    @PutMapping("/{boardId}/end")
    public ResponseEntity<ResultDto> end(@PathVariable int boardId) {
        return ResponseEntity.ok(chessGameService.getFinalResult(boardId));
    }
}
