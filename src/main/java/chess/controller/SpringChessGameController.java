package chess.controller;

import chess.domain.ChessGame;
import chess.dto.game.GameDTO;
import chess.exception.InitialSettingDataException;
import chess.exception.NoHistoryException;
import chess.exception.NotEnoughPlayerException;
import chess.exception.RoomException;
import chess.service.HistoryService;
import chess.service.ResultService;
import chess.service.RoomService;
import chess.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public final class SpringChessGameController {
    private final RoomService roomService;
    private final ResultService resultService;
    private final UserService userService;
    private final HistoryService historyService;

    public SpringChessGameController(final RoomService roomService, final ResultService resultService,
                                     final UserService userService, final HistoryService historyService) {
        this.roomService = roomService;
        this.resultService = resultService;
        this.userService = userService;
        this.historyService = historyService;
    }

    @GetMapping("/")
    public String goHome(final Model model) {
        roomService.loadRooms();
        model.addAttribute("rooms", roomService.allRooms());
        model.addAttribute("results", resultService.allUserResult());
        return "index";
    }

    @GetMapping("/rooms/{id}")
    public String enterRoom(@PathVariable final String id, final Model model) {
        model.addAttribute("state",
                new GameDTO(id, userService.participatedUsers(id), "새로운게임", true));
        return "chess";
    }

    @GetMapping(path = "/rooms/{id}/pieces")
    public String startGame(@PathVariable final String id, final Model model) {
        roomService.addNewRoom(id);
        historyService.initializeByRoomId(id);
        model.addAttribute("state",
                new GameDTO(id, userService.usersParticipatedInGame(id), roomService.loadChessGameById(id), "초기화")
        );
        return "chess";
    }

    @GetMapping(path = "/rooms/{id}/saved-pieces")
    public String continueGame(@PathVariable final String id, final Model model) {
        ChessGame chessGame = roomService.initializeChessGame(id);
        historyService.continueGame(id, chessGame);
        model.addAttribute("state",
                new GameDTO(id, userService.usersParticipatedInGame(id), roomService.loadChessGameById(id), "초기화")
        );
        return "chess";
    }

    @GetMapping(path = "/error-page/{code}")
    public String errorPage(@PathVariable final String code) {
        return "/error/" + code + ".html";
    }

    @ExceptionHandler(InitialSettingDataException.class)
    public String initSettingException() {
        return "/error/500.html";
    }

    @ExceptionHandler({NoHistoryException.class, NotEnoughPlayerException.class})
    public String notExistHistory(final RoomException e, final Model model) {
        String roomId = e.getRoomId();
        model.addAttribute("state",
                new GameDTO(roomId, "새로운게임", userService.participatedUsers(roomId), e.getMessage()));
        return "chess";
    }
}
