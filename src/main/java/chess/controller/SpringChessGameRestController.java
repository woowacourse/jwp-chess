package chess.controller;

import chess.domain.ChessGame;
import chess.dto.MoveDTO;
import chess.dto.ResultDTO;
import chess.dto.SectionDTO;
import chess.dto.StatusDTO;
import chess.dto.UsersDTO;
import chess.service.HistoryService;
import chess.service.ResultService;
import chess.service.RoomService;
import chess.service.UserService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
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
    private final HistoryService historyService;

    public SpringChessGameRestController(final RoomService roomService,
        final ResultService resultService,
        final UserService userService, final HistoryService historyService) {
        this.roomService = roomService;
        this.resultService = resultService;
        this.userService = userService;
        this.historyService = historyService;
    }

    @PostMapping(path = "/turn")
    public ResponseEntity<Boolean> checkCurrentTurn(
        @RequestBody final SectionDTO sectionDTO,
        @CookieValue(value = "password", required = false) final String password) {

        boolean isCurrentPlayerTurn = roomService
            .checkRightTurn(sectionDTO.getRoomId(), sectionDTO.getClickedSection(), password);

        if (isCurrentPlayerTurn) {
            return ResponseEntity.status(HttpStatus.OK).body(true);
        }
        return ResponseEntity.status(HttpStatus.OK).body(false);
    }

    @PostMapping("/movable-positions")
    public ResponseEntity<List<String>> findMovablePosition(@RequestBody final SectionDTO sectionDTO) {
        ChessGame chessGame = roomService.loadGameByRoomId(sectionDTO.getRoomId());
        List<String> positions = chessGame.movablePositionsByStartPoint(sectionDTO.getClickedSection());
        return ResponseEntity.status(HttpStatus.OK).body(positions);
    }

    @PostMapping("/move")
    public ResponseEntity<Object> movePiece(@RequestBody final MoveDTO moveDTO) {
        String roomId = moveDTO.getRoomId();
        String startPoint = moveDTO.getStartPoint();
        String endPoint = moveDTO.getEndPoint();
        roomService.move(roomId, startPoint, endPoint);

        historyService.createLog(roomId, startPoint, endPoint);
        UsersDTO users = userService.usersParticipatedInGame(roomId);
        StatusDTO statusDTO = new StatusDTO(roomService.loadGameByRoomId(roomId), users);

        return ResponseEntity.status(HttpStatus.OK).body(statusDTO);
    }

    @PostMapping(path = "/initialize")
    public ResponseEntity<Boolean> initialize(@RequestBody final ResultDTO resultDTO) {
        String roomId = resultDTO.getRoomId();
        String winner = resultDTO.getWinner();
        String loser = resultDTO.getLoser();

        roomService.changeStatus(roomId);
        int winnerId = userService.userIdByNickname(winner);
        int loserId = userService.userIdByNickname(loser);

        resultService.saveGameResult(roomId, winnerId, loserId);
        return ResponseEntity.status(HttpStatus.OK).body(true);
    }
}
