package chess.controller.spring;

import chess.domain.board.ChessBoard;
import chess.domain.piece.TeamType;
import chess.domain.result.Result;
import chess.dto.MoveRequestDTO;
import chess.dto.ResultDTO;
import chess.dto.board.BoardDTO;
import chess.service.spring.ChessService;
import chess.service.spring.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/chessgame/{id}")
public class ChessController {

    private final ChessService chessService;
    private final UserService userService;

    public ChessController(ChessService chessService, UserService userService) {
        this.chessService = chessService;
        this.userService = userService;
    }

    @GetMapping("/chessboard")
    public ResponseEntity<BoardDTO> showChessBoard(@PathVariable int id) {
        BoardDTO chessBoardDTO = findChessBoard(id);
        return writeResponse(chessBoardDTO);
    }

    private BoardDTO findChessBoard(int id) {
        ChessBoard chessBoard = chessService.findChessBoardByRoomId(id);
        TeamType teamType = chessService.findCurrentTeamTypeByRoomId(id);
        return BoardDTO.of(chessBoard, teamType);
    }

    private <T> ResponseEntity<T> writeResponse(T t) {
        return ResponseEntity.ok().body(t);
    }

    @PutMapping("/chessboard")
    public ResponseEntity<BoardDTO> move(@PathVariable int id, @RequestBody MoveRequestDTO moveRequestDTO, HttpSession httpSession) {
        String password = (String) httpSession.getAttribute("password");
        TeamType currentTeamType = chessService.findCurrentTeamTypeByRoomId(id);
        userService.validateCurrentUser(id, password, currentTeamType);
        String current = moveRequestDTO.getCurrent();
        String destination = moveRequestDTO.getDestination();
        String teamType = moveRequestDTO.getTeamType();
        chessService.moveByRoomId(current, destination, teamType, id);
        BoardDTO chessBoardDTO = findChessBoard(id);
        return writeResponse(chessBoardDTO);
    }

    @GetMapping("/result")
    public ResponseEntity<ResultDTO> showResult(@PathVariable int id) {
        Result result = chessService.calculateResultByRoomId(id);
        ResultDTO resultDTO = ResultDTO.from(result);
        return writeResponse(resultDTO);
    }

    @DeleteMapping("/histories")
    public ResponseEntity<String> restart(@PathVariable int id) {
        chessService.deleteAllHistoriesByRoomId(id);
        String location = "/";
        return writeResponse(location);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleException(RuntimeException runtimeException) {
        return ResponseEntity.status(400).body(runtimeException.getMessage());
    }
}
