package chess.controller;

import chess.domain.ChessGame;
import chess.domain.Rooms;
import chess.domain.Team;
import chess.dto.*;
import chess.exception.DataAccessException;
import chess.exception.DriverLoadException;
import chess.service.LogService;
import chess.service.ResultService;
import chess.service.RoomService;
import chess.service.UserService;
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
    private boolean createNewGame(@RequestBody final CreateRoomRequestDTO createRoomRequestDTO) {
        roomService.createRoom(createRoomRequestDTO.getName());
        return true;
    }

    @GetMapping("/enter")
    private String enterRoom(@RequestParam final String id, final Model model) {
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

    @PostMapping(path = "/start")
    private String startGame(@ModelAttribute final RoomIdDTO roomIdDTO, final Model model) {
        ChessGame chessGame = new ChessGame();
        chessGame.initialize();
        String roomId = roomIdDTO.getRoomId();
        rooms.addRoom(roomId, chessGame);
        logService.initializeByRoomId(roomId);
        UsersDTO users = userService.usersParticipatedInGame(roomId);
        gameInformation(rooms.loadGameByRoomId(roomId), model, roomId, users);
        return "chess";
    }

    @PostMapping(path = "/continue")
    private String continueGame(@ModelAttribute final RoomIdDTO roomIdDTO, final Model model) {
        String roomId = roomIdDTO.getRoomId();
        ChessGame chessGame = rooms.loadGameByRoomId(roomId);
        chessGame.initialize();
        List<String[]> logs = logService.logByRoomId(roomId);
        logService.executeLog(logs, chessGame);
        UsersDTO users = userService.usersParticipatedInGame(roomId);
        gameInformation(rooms.loadGameByRoomId(roomId), model, roomId, users);
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
        model.addAttribute("number", roomId);
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
    private boolean initialize(@RequestBody final InitializeDTO initializeDTO) {
        String roomId = initializeDTO.getRoomId();
        String winner = initializeDTO.getWinner();
        String loser = initializeDTO.getLoser();
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
    @ResponseBody
    private ResponseEntity dataAccessExceptionHandle(DataAccessException e) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(e.getMessage());
    }

    @GetMapping(path = "/errorPage")
    private String errorPage(@RequestParam final String error, final Model model) {
        model.addAttribute("error", error);
        return "error";
    }
}
