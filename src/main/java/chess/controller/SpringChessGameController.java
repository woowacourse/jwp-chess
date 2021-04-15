package chess.controller;

import chess.domain.ChessGame;
import chess.domain.Rooms;
import chess.domain.Team;
import chess.dto.*;
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

@Controller
public final class SpringChessGameController {
    private final Rooms rooms = new Rooms();
    private final RoomService roomService;
    private final ResultService resultService;
    private final UserService userService;
    private final LogService logService;

    public SpringChessGameController(RoomService roomService, ResultService resultService, UserService userService, LogService logService) {
        this.roomService = roomService;
        this.resultService = resultService;
        this.userService = userService;
        this.logService = logService;
    }

    @GetMapping("/")
    private String goHome(Model model) {
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
    private boolean createNewGame(@RequestBody CreateRoomRequestDTO createRoomRequestDTO) {
        roomService.createRoom(createRoomRequestDTO.getName());
        return true;
    }

    @GetMapping("/enter")
    private String enterRoom(@RequestParam String id, Model model) {
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
    private String startGame(@ModelAttribute RoomIdDTO roomIdDTO, Model model) {
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
    private String continueGame(@ModelAttribute RoomIdDTO roomIdDTO, Model model) {
        ChessGame chessGame = new ChessGame();
        chessGame.initialize();
        String roomId = roomIdDTO.getRoomId();
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
    private boolean checkCurrentTurn(@RequestBody SectionDTO sectionDTO) {
        ChessGame chessGame = rooms.loadGameByRoomId(sectionDTO.getRoomId());
        return chessGame.checkRightTurn(sectionDTO.getClickedSection());

    }

    @PostMapping("/movablePositions")
    @ResponseBody
    private List<String> findMovablePosition(@RequestBody SectionDTO sectionDTO) {
        ChessGame chessGame = rooms.loadGameByRoomId(sectionDTO.getRoomId());
        return chessGame.movablePositionsByStartPoint(sectionDTO.getClickedSection());
    }

    @PostMapping("/move")
    @ResponseBody
    private StatusDTO movePiece(@RequestBody MoveDTO moveDTO) {
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
    private boolean initialize(@RequestBody InitializeDTO initializeDTO) {
        String roomId = initializeDTO.getRoomId();
        String winner = initializeDTO.getWinner();
        String loser = initializeDTO.getLoser();
        roomService.changeStatus(roomId);
        int winnerId = userService.userIdByNickname(winner);
        int loserId = userService.userIdByNickname(loser);
        resultService.saveGameResult(roomId, winnerId, loserId);
        return true;
    }
}
