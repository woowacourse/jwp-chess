package chess.controller;

import chess.domain.ChessGame;
import chess.dto.game.MoveDTO;
import chess.dto.game.StatusDTO;
import chess.dto.player.PlayerDTO;
import chess.dto.player.PlayersDTO;
import chess.dto.result.ResultDTO;
import chess.service.HistoryService;
import chess.service.PlayerService;
import chess.service.ResultService;
import chess.service.RoomService;
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/rooms")
public class SpringChessGameRestController {
    private final RoomService roomService;
    private final ResultService resultService;
    private final PlayerService playerService;
    private final HistoryService historyService;

    public SpringChessGameRestController(final RoomService roomService, final ResultService resultService,
                                         final PlayerService playerService, final HistoryService historyService) {
        this.roomService = roomService;
        this.resultService = resultService;
        this.playerService = playerService;
        this.historyService = historyService;
    }

    @GetMapping(path = "/{roomId}/positions/{clickedSection}/turn")
    public ResponseEntity<Boolean> checkCurrentTurn(@PathVariable final String roomId, @PathVariable final String clickedSection,
                                                    final HttpSession session) {
        String id = (String) session.getAttribute("id");
        String password = (String) session.getAttribute("password");

        PlayerDTO user = playerService.getUser(id, password);
        if (!roomService.checkRightTurn(roomId, user, clickedSection)) {
            return ResponseEntity.status(OK)
                    .body(false);
        }
        return ResponseEntity.status(OK)
                .body(true);
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
        PlayersDTO users = playerService.usersParticipatedInGame(roomId);
        return ResponseEntity.status(OK)
                .body(new StatusDTO(chessGame, users));
    }

    @PostMapping("/{roomId}/end")
    public ResponseEntity<Boolean> endGame(@PathVariable final String roomId, @RequestBody @Valid final ResultDTO resultDTO) {
        String winner = resultDTO.getWinner();
        String loser = resultDTO.getLoser();
        roomService.changeStatus(roomId);
        int winnerId = playerService.userIdByNickname(winner);
        int loserId = playerService.userIdByNickname(loser);
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
