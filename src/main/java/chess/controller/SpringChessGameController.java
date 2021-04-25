package chess.controller;

import chess.domain.ChessGame;
import chess.dto.game.GameDTO;
import chess.dto.room.RoomCreateDTO;
import chess.dto.user.JoinUserDTO;
import chess.dto.user.PasswordDTO;
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
import org.springframework.web.bind.annotation.*;

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
    public String home(final Model model) {
        roomService.loadRooms();
        model.addAttribute("rooms", roomService.allRooms());
        model.addAttribute("results", resultService.allUserResult());
        return "index";
    }

    @PostMapping(path = "/rooms/new-game")
    public String createNewGame(@ModelAttribute final RoomCreateDTO roomCreateDTO) {
        int whiteUserId = userService.registerUser(new JoinUserDTO(roomCreateDTO));
        Long roomId = roomService.createRoom(roomCreateDTO.getName(), whiteUserId);
        return "redirect:/rooms/" + roomId;
    }

    @PostMapping("/rooms/{id}/users/blackuser/add")
    public String join(@PathVariable String id, @ModelAttribute JoinUserDTO joinUserDTO) {
        int blackUserId = userService.registerUser(joinUserDTO);
        roomService.joinBlackUser(id, blackUserId);
        return "redirect:/rooms/" + id;
    }

    @PostMapping("/rooms/{id}/users/blackuser/re-enter")
    public String blackUserReEntry(@PathVariable String id, @ModelAttribute PasswordDTO passwordDTO) {
        String blackUserId = roomService.findBlackUserById(id);
        if (userService.checkPassword(blackUserId, passwordDTO.getPassword())) {
            return "redirect:/rooms/" + id;
        }
        return "redirect:/";
    }

    @PostMapping("/rooms/{id}/users/whiteuser/re-enter")
    public String whiteUserReEntry(@PathVariable String id, @ModelAttribute PasswordDTO passwordDTO) {
        String whiteUserId = roomService.findWhiteUserById(id);
        System.out.println("##");
        System.out.println(whiteUserId);
        if (userService.checkPassword(whiteUserId, passwordDTO.getPassword())) {
            return "redirect:/rooms/" + id;
        }
        return "redirect:/";
    }

    @GetMapping("/rooms/{id}")
    public String enterRoom(@PathVariable final String id, final Model model) {
        model.addAttribute("state",
                new GameDTO(id, userService.participatedUsers(id), "새로운게임", true));
        return "chess";
    }

    @GetMapping(path = "/rooms/{id}/pieces")
    public String initializePieces(@PathVariable final String id, final Model model) {
        roomService.addNewRoom(id);
        historyService.initializeByRoomId(id);
        model.addAttribute("state",
                new GameDTO(id, userService.usersParticipatedInGame(id), roomService.loadChessGameById(id), "초기화")
        );
        return "chess";
    }

    @GetMapping(path = "/rooms/{id}/saved-pieces")
    public String savedPieces(@PathVariable final String id, final Model model) {
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
