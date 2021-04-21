package chess.controller;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import chess.domain.ChessGame;
import chess.dto.MoveDTO;
import chess.dto.ResultDTO;
import chess.dto.RoomNameDTO;
import chess.dto.SectionDTO;
import chess.dto.StatusDTO;
import chess.dto.UsersDTO;
import chess.exception.DriverLoadException;
import chess.service.LogService;
import chess.service.ResultService;
import chess.service.RoomService;
import chess.service.UserService;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chess")
public class SpringChessGameRestController {

    private final RoomService roomService;
    private final ResultService resultService;
    private final UserService userService;
    private final LogService logService;

    public SpringChessGameRestController(final RoomService roomService,
        final ResultService resultService,
        final UserService userService, final LogService logService) {
        this.roomService = roomService;
        this.resultService = resultService;
        this.userService = userService;
        this.logService = logService;
    }

    @PostMapping(path = "/new-game", consumes = "application/json")
    private boolean createNewGame(@RequestBody final RoomNameDTO roomNameDTO) {
        roomService.createRoom(roomNameDTO.getName());
        return true;
    }

    @PostMapping(path = "/turn", consumes = "application/json")
    private boolean checkCurrentTurn(@RequestBody final SectionDTO sectionDTO) {
        ChessGame chessGame = roomService.loadGameByRoomId(sectionDTO.getRoomId());
        return chessGame.checkRightTurn(sectionDTO.getClickedSection());
    }

    @PostMapping("/movable-positions")
    private List<String> findMovablePosition(@RequestBody final SectionDTO sectionDTO) {
        ChessGame chessGame = roomService.loadGameByRoomId(sectionDTO.getRoomId());
        return chessGame.movablePositionsByStartPoint(sectionDTO.getClickedSection());
    }

    @PostMapping("/move")
    private StatusDTO movePiece(@RequestBody final MoveDTO moveDTO) {
        String roomId = moveDTO.getRoomId();
        String startPoint = moveDTO.getStartPoint();
        String endPoint = moveDTO.getEndPoint();
        ChessGame chessGame = roomService.loadGameByRoomId(roomId);
        chessGame.move(startPoint, endPoint);
        logService.createLog(roomId, startPoint, endPoint);
        UsersDTO users = userService.usersParticipatedInGame(roomId);
        return new StatusDTO(chessGame, users);
    }

    @PostMapping(path = "/initialize")
    private boolean initialize(@RequestBody final ResultDTO resultDTO) {
        String roomId = resultDTO.getRoomId();
        String winner = resultDTO.getWinner();
        String loser = resultDTO.getLoser();
        roomService.changeStatus(roomId);
        int winnerId = userService.userIdByNickname(winner);
        int loserId = userService.userIdByNickname(loser);
        resultService.saveGameResult(roomId, winnerId, loserId);
        return true;
    }

    @ExceptionHandler(DriverLoadException.class)
    private ResponseEntity driverLoadExceptionHandle() {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body("!! JDBC Driver load 오류");
    }

    @ExceptionHandler(DataAccessException.class)
    private ResponseEntity dataAccessExceptionHandle(DataAccessException e) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body("!! Database Access 오류");
    }
}
