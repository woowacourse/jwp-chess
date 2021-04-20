package chess.controller;

import chess.domain.ChessGame;
import chess.domain.Team;
import chess.dto.game.PiecesDTO;
import chess.dto.user.UsersDTO;
import chess.exception.InitialSettingDataException;
import chess.exception.NoLogsException;
import chess.exception.NotEnoughPlayerException;
import chess.exception.RoomException;
import chess.service.LogService;
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
    private final LogService logService;

    public SpringChessGameController(final RoomService roomService, final ResultService resultService,
                                     final UserService userService, final LogService logService) {
        this.roomService = roomService;
        this.resultService = resultService;
        this.userService = userService;
        this.logService = logService;
    }

    @GetMapping("/")
    public String goHome(final Model model) {
        roomService.loadRooms();
        model.addAttribute("rooms", roomService.allRooms());
        model.addAttribute("results", resultService.allUserResult());
        return "index";
    }

    @GetMapping("/room/{id}")
    public String enterRoom(@PathVariable final String id, final Model model) {
        model.addAttribute("number", id);
        model.addAttribute("button", "새로운게임");
        model.addAttribute("isWhite", true);
        model.addAttribute("users", userService.participatedUsers(id));
        return "chess";
    }

    @GetMapping(path = "/room/{id}/play")
    public String startGame(@PathVariable final String id, final Model model) {
        roomService.addNewRoom(id);
        logService.initializeByRoomId(id);
        UsersDTO users = userService.usersParticipatedInGame(id);
        gameInformation(roomService.loadChessGameById(id), model, id, users);
        return "chess";
    }

    @GetMapping(path = "/room/{id}/continue")
    public String continueGame(@PathVariable final String id, final Model model) {
        ChessGame chessGame = roomService.initializeChessGame(id);
        logService.continueGame(id, chessGame);
        UsersDTO users = userService.usersParticipatedInGame(id);
        gameInformation(chessGame, model, id, users);
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

    @GetMapping(path = "/error-page/{code}")
    public String errorPage(@PathVariable final String code) {
        return "/error/" + code + ".html";
    }

    @ExceptionHandler(InitialSettingDataException.class)
    public String initSettingException() {
        return "/error/500.html";
    }

    @ExceptionHandler({NoLogsException.class, NotEnoughPlayerException.class})
    public String notExistLog(final RoomException e, final Model model) {
        String roomId = e.getRoomId();
        model.addAttribute("id", roomId);
        model.addAttribute("button", "새로운게임");
        model.addAttribute("users", userService.participatedUsers(roomId));
        model.addAttribute("error", e.getMessage());
        return "chess";
    }
}
