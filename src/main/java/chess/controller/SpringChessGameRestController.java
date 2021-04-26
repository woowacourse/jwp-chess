package chess.controller;

import chess.domain.ChessGame;
import chess.dto.CreateRoomRequestDTO;
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
import org.springframework.web.bind.annotation.ModelAttribute;
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

    @PostMapping(path = "/turn", consumes = "application/json")
    public boolean checkCurrentTurn(@RequestBody final SectionDTO sectionDTO) {
        ChessGame chessGame = roomService.loadGameByRoomId(sectionDTO.getRoomId());
        return chessGame.checkRightTurn(sectionDTO.getClickedSection());
    }

    @PostMapping("/movable-positions")
    public List<String> findMovablePosition(@RequestBody final SectionDTO sectionDTO) {
        ChessGame chessGame = roomService.loadGameByRoomId(sectionDTO.getRoomId());
        return chessGame.movablePositionsByStartPoint(sectionDTO.getClickedSection());
    }

    @PostMapping("/move")
    public StatusDTO movePiece(@RequestBody final MoveDTO moveDTO) {
        String roomId = moveDTO.getRoomId();
        String startPoint = moveDTO.getStartPoint();
        String endPoint = moveDTO.getEndPoint();

        ChessGame chessGame = roomService.loadGameByRoomId(roomId);
        chessGame.move(startPoint, endPoint);

        historyService.createLog(roomId, startPoint, endPoint);
        UsersDTO users = userService.usersParticipatedInGame(roomId);
        return new StatusDTO(chessGame, users);
    }

    @PostMapping(path = "/initialize")
    public boolean initialize(@RequestBody final ResultDTO resultDTO) {
        String roomId = resultDTO.getRoomId();
        String winner = resultDTO.getWinner();
        String loser = resultDTO.getLoser();

        roomService.changeStatus(roomId);
        int winnerId = userService.userIdByNickname(winner);
        int loserId = userService.userIdByNickname(loser);

        resultService.saveGameResult(roomId, winnerId, loserId);
        return true;
    }
}
