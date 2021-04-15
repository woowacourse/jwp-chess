package chess.controller;

import chess.domain.ChessGame;
import chess.domain.Rooms;
import chess.domain.Team;
import chess.dto.CreateRoomRequestDTO;
import chess.dto.PiecesDTO;
import chess.dto.RoomIdDTO;
import chess.dto.UsersDTO;
import chess.service.LogService;
import chess.service.ResultService;
import chess.service.RoomService;
import chess.service.UserService;
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
}
