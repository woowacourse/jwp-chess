package chess.controller;

import chess.domain.ChessGame;
import chess.domain.Movement;
import chess.domain.Rooms;
import chess.domain.Team;
import chess.dto.*;
import chess.exception.DriverLoadException;
import chess.service.LogService;
import chess.service.ResultService;
import chess.service.RoomService;
import chess.service.UserService;
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Controller
public final class SpringChessGameController {
    private final Rooms rooms = new Rooms();
    private final RoomService roomService;
    private final ResultService resultService;
    private final UserService userService;
    private final LogService logService;

    public SpringChessGameController(final RoomService roomService, final ResultService resultService,
                                     final UserService userService, final LogService logService) {
        this.roomService = roomService;
        this.resultService = resultService;
        this.userService = userService;
        this.logService = logService;
    }

    @GetMapping("/")
    private String goHome(final Model model) {
        try {
            List<String> roomIds = roomService.allRoomsId();
            roomIds.forEach(id -> rooms.addRoom(id, new ChessGame()));
            model.addAttribute("rooms", roomService.allRooms());
            model.addAttribute("results", resultService.allUserResult());
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "index";
    }

    @PostMapping(path = "/createNewGame", consumes = "application/json")
    @ResponseBody
    private boolean createNewGame(@RequestBody final RoomNameDTO roomNameDTO) {
        roomService.createRoom(roomNameDTO.getName());
        return true;
    }

    @GetMapping("/enter/{id}")
    private String enterRoom(@PathVariable final String id, final Model model) {
        try {
            model.addAttribute("number", id);
            model.addAttribute("button", "새로운게임");
            model.addAttribute("isWhite", true);
            model.addAttribute("users", userService.usersParticipatedInGame(id));
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "chess";
    }

    @GetMapping(path = "/start/{id}")
    private String startGame(@PathVariable final String id, final Model model) {
        ChessGame chessGame = new ChessGame();
        chessGame.initialize();
        rooms.addRoom(id, chessGame);
        logService.initializeByRoomId(id);
        UsersDTO users = userService.usersParticipatedInGame(id);
        gameInformation(rooms.loadGameByRoomId(id), model, id, users);
        return "chess";
    }

    @GetMapping(path = "/continue/{id}")
    private String continueGame(@PathVariable final String id, final Model model) {
        ChessGame chessGame = rooms.loadGameByRoomId(id);
        chessGame.initialize();
        List<Movement> logs = logService.logByRoomId(id);
        logService.executeLog(logs, chessGame);
        UsersDTO users = userService.usersParticipatedInGame(id);
        gameInformation(rooms.loadGameByRoomId(id), model, id, users);
        return "chess";
    }

    private void gameInformation(final ChessGame chessGame, final Model model,
                                 final String roomId, final UsersDTO users) {
        PiecesDTO piecesDTOs = PiecesDTO.create(chessGame.board());
        model.addAttribute("pieces", piecesDTOs.toList());
        model.addAttribute("button", "초기화");
        model.addAttribute("isWhite", Team.WHITE.equals(chessGame.turn()));
        model.addAttribute("black-score", chessGame.scoreByTeam(Team.BLACK));
        model.addAttribute("white-score", chessGame.scoreByTeam(Team.WHITE));
        model.addAttribute("id", roomId);
        model.addAttribute("users", users);
    }

    @PostMapping(path = "/myTurn")
    @ResponseBody
    private boolean checkCurrentTurn(@RequestBody final SectionDTO sectionDTO) {
        ChessGame chessGame = rooms.loadGameByRoomId(sectionDTO.getRoomId());
        return chessGame.checkRightTurn(sectionDTO.getClickedSection());

    }

    @PostMapping("/movablePositions")
    @ResponseBody
    private List<String> findMovablePosition(@RequestBody final SectionDTO sectionDTO) {
        ChessGame chessGame = rooms.loadGameByRoomId(sectionDTO.getRoomId());
        return chessGame.movablePositionsByStartPoint(sectionDTO.getClickedSection());
    }

    @PostMapping("/move")
    @ResponseBody
    private StatusDTO movePiece(@RequestBody final MoveDTO moveDTO) {
        String roomId = moveDTO.getRoomId();
        String startPoint = moveDTO.getStartPoint();
        String endPoint = moveDTO.getEndPoint();
        ChessGame chessGame = rooms.loadGameByRoomId(roomId);
        chessGame.move(startPoint, endPoint);
        logService.createLog(roomId, startPoint, endPoint);
        UsersDTO users = userService.usersParticipatedInGame(roomId);
        return new StatusDTO(chessGame, users);
    }

    @PostMapping(path = "/initialize")
    @ResponseBody
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
    @ResponseBody
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

    @GetMapping(path = "/errorPage")
    private String errorPage(@RequestParam final String error, final Model model) {
        model.addAttribute("error", error);
        return "error";
    }
}
