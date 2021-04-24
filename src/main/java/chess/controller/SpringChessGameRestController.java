package chess.controller;

import chess.domain.ChessGame;
import chess.dto.game.MoveDTO;
import chess.dto.game.StatusDTO;
import chess.dto.result.ResultDTO;
import chess.dto.user.UsersDTO;
import chess.service.HistoryService;
import chess.service.ResultService;
import chess.service.RoomService;
import chess.service.UserService;
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/rooms")
public class SpringChessGameRestController {
    private final RoomService roomService;
    private final ResultService resultService;
    private final UserService userService;
    private final HistoryService historyService;

    public SpringChessGameRestController(final RoomService roomService, final ResultService resultService,
                                         final UserService userService, final HistoryService historyService) {
        this.roomService = roomService;
        this.resultService = resultService;
        this.userService = userService;
        this.historyService = historyService;
    }

    @GetMapping(path = "/{roomId}/positions/{clickedSection}/turn")
    public ResponseEntity<Boolean> checkCurrentTurn(@PathVariable final String roomId, @PathVariable final String clickedSection) {
        ChessGame chessGame = roomService.loadChessGameById(roomId);
        return ResponseEntity.status(OK)
                .body(chessGame.checkRightTurn(clickedSection));
    }

    @GetMapping("/{roomId}/positions/{clickedSection}/movable-positions")
    public ResponseEntity<List<String>> findMovablePosition(@PathVariable final String roomId, @PathVariable final String clickedSection) {
        ChessGame chessGame = roomService.loadChessGameById(roomId);
        return ResponseEntity.status(OK)
                .body(chessGame.movablePositionsByStartPoint(clickedSection));
    }

    @PostMapping("/{roomId}/piece/move")
    public ResponseEntity<StatusDTO> movePiece(@PathVariable final String roomId, @RequestBody @Valid final MoveDTO moveDTO) {
        String startPoint = moveDTO.getStartPoint();
        String endPoint = moveDTO.getEndPoint();
        ChessGame chessGame = roomService.movePiece(roomId, startPoint, endPoint);
        historyService.createHistory(roomId, startPoint, endPoint);
        UsersDTO users = userService.usersParticipatedInGame(roomId);
        return ResponseEntity.status(OK)
                .body(new StatusDTO(chessGame, users));
    }

    @PostMapping("/{roomId}/end")
    public ResponseEntity<Boolean> endGame(@PathVariable final String roomId, @RequestBody @Valid final ResultDTO resultDTO) {
        String winner = resultDTO.getWinner();
        String loser = resultDTO.getLoser();
        roomService.changeStatus(roomId);
        int winnerId = userService.userIdByNickname(winner);
        int loserId = userService.userIdByNickname(loser);
        resultService.saveGameResult(roomId, winnerId, loserId);
        return ResponseEntity.status(OK).body(true);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> clientExceptionHandle() {
        return ResponseEntity.status(BAD_REQUEST)
                .body("client error");
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> dataAccessExceptionHandle() {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body("!! Database Access 오류");
    }
}
