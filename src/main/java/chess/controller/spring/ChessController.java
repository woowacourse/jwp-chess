package chess.controller.spring;

import chess.domain.board.ChessBoard;
import chess.domain.piece.TeamType;
import chess.domain.result.Result;
import chess.dto.MoveRequestDTO;
import chess.dto.ResultDTO;
import chess.dto.board.BoardDTO;
import chess.service.spring.ChessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chessgame")
public class ChessController {

    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/chessboard")
    public BoardDTO showChessBoard() {
        return findChessBoard();
    }

    private BoardDTO findChessBoard() {
        ChessBoard chessBoard = chessService.findChessBoard();
        TeamType teamType = chessService.findCurrentTeamType();
        return BoardDTO.of(chessBoard, teamType);
    }

    @PutMapping("/chessboard")
    public BoardDTO move(@RequestBody MoveRequestDTO moveRequestDTO) {
        String current = moveRequestDTO.getCurrent();
        String destination = moveRequestDTO.getDestination();
        String teamType = moveRequestDTO.getTeamType();
        chessService.move(current, destination, teamType);
        return findChessBoard();
    }

    @GetMapping("/result")
    public ResultDTO showResult() {
        Result result = chessService.calculateResult();
        return ResultDTO.from(result);
    }

    @DeleteMapping("/histories")
    public ResponseEntity<String> restart() {
        chessService.deleteAllHistories();
        return ResponseEntity.ok().body("/");
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleException(RuntimeException runtimeException) {
        return ResponseEntity.status(500).body(runtimeException.getMessage());
    }
}
