package chess.controller;

import chess.domain.ChessGame;
import chess.dto.*;
import chess.exception.ClientException;
import chess.service.LogService;
import chess.service.ResultService;
import chess.service.RoomService;
import chess.service.UserService;
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
public class SpringChessGameRestController {
    private final RoomService roomService;
    private final ResultService resultService;
    private final UserService userService;
    private final LogService logService;

    public SpringChessGameRestController(final RoomService roomService, final ResultService resultService,
                                         final UserService userService, final LogService logService) {
        this.roomService = roomService;
        this.resultService = resultService;
        this.userService = userService;
        this.logService = logService;
    }

    @PostMapping(path = "/new-game")
    public ResponseEntity<Boolean> createNewGame(@RequestBody final RoomNameDTO roomNameDTO) {
        roomService.createRoom(roomNameDTO.getName());
        return ResponseEntity.status(OK).body(true);
    }

    @PostMapping(path = "/turn")
    public ResponseEntity<Boolean> checkCurrentTurn(@RequestBody final SectionDTO sectionDTO) {
        ChessGame chessGame = roomService.loadChessGameById(sectionDTO.getRoomId());
        return ResponseEntity.status(OK)
                .body(chessGame.checkRightTurn(sectionDTO.getClickedSection()));
    }

    @PostMapping("/movable-positions")
    public ResponseEntity<List<String>> findMovablePosition(@RequestBody final SectionDTO sectionDTO) {
        ChessGame chessGame = roomService.loadChessGameById(sectionDTO.getRoomId());
        return ResponseEntity.status(OK)
                .body(chessGame.movablePositionsByStartPoint(sectionDTO.getClickedSection()));
    }

    @PostMapping("/piece/move")
    public ResponseEntity<StatusDTO> movePiece(@RequestBody final MoveDTO moveDTO) {
        String roomId = moveDTO.getRoomId();
        String startPoint = moveDTO.getStartPoint();
        String endPoint = moveDTO.getEndPoint();
        ChessGame chessGame = roomService.movePiece(roomId, startPoint, endPoint);
        logService.createLog(roomId, startPoint, endPoint);
        UsersDTO users = userService.usersParticipatedInGame(roomId);
        return ResponseEntity.status(OK)
                .body(new StatusDTO(chessGame, users));
    }

    @PostMapping(path = "/game/end")
    public ResponseEntity<Boolean> initialize(@RequestBody final ResultDTO resultDTO) {
        String roomId = resultDTO.getRoomId();
        String winner = resultDTO.getWinner();
        String loser = resultDTO.getLoser();
        roomService.changeStatus(roomId);
        int winnerId = userService.userIdByNickname(winner);
        int loserId = userService.userIdByNickname(loser);
        resultService.saveGameResult(roomId, winnerId, loserId);
        return ResponseEntity.status(OK).body(true);
    }

    @ExceptionHandler(ClientException.class)
    public ResponseEntity<String> clientException() {
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
