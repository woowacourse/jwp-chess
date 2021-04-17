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
    public ResponseEntity<BoardDTO> showChessBoard() {
        BoardDTO chessBoardDTO = findChessBoard();
        return writeResponse(chessBoardDTO);
    }

    private BoardDTO findChessBoard() {
        ChessBoard chessBoard = chessService.findChessBoard();
        TeamType teamType = chessService.findCurrentTeamType();
        return BoardDTO.of(chessBoard, teamType);
    }

    private <T> ResponseEntity<T> writeResponse(T t) {
        return ResponseEntity.ok().body(t);
    }

    @PutMapping("/chessboard")
    public ResponseEntity<BoardDTO> move(@RequestBody MoveRequestDTO moveRequestDTO) {
        String current = moveRequestDTO.getCurrent();
        String destination = moveRequestDTO.getDestination();
        String teamType = moveRequestDTO.getTeamType();
        chessService.move(current, destination, teamType);
        BoardDTO chessBoardDTO = findChessBoard();
        return writeResponse(chessBoardDTO);
    }

    @GetMapping("/result")
    public ResponseEntity<ResultDTO> showResult() {
        Result result = chessService.calculateResult();
        ResultDTO resultDTO = ResultDTO.from(result);
        return writeResponse(resultDTO);
    }

    @DeleteMapping("/histories")
    public ResponseEntity<String> restart() {
        chessService.deleteAllHistories();
        String location = "/";
        return writeResponse(location);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleException(RuntimeException runtimeException) {
        return ResponseEntity.status(500).body(runtimeException.getMessage());
    }
}
