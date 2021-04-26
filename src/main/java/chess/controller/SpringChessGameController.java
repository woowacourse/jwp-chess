package chess.controller;

import chess.domain.ChessGame;
import chess.dto.game.GameDTO;
import chess.dto.room.RoomCreateDTO;
import chess.dto.user.JoinUserDTO;
import chess.dto.user.PasswordDTO;
import chess.dto.user.UserDTO;
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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import static chess.dao.UserDAO.UNKNOWN_USER;

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

    @PostMapping(path = "/rooms")
    public String createNewGame(@ModelAttribute final RoomCreateDTO roomCreateDTO, final HttpServletResponse response) {
        int whiteUserId = userService.registerUser(new JoinUserDTO(roomCreateDTO));
        Long roomId = roomService.createRoom(roomCreateDTO.getName(), whiteUserId);
        roomService.addNewRoom(roomId);
        playerCookie(response, roomCreateDTO.getNickname(), roomCreateDTO.getPassword(), "white");
        return "redirect:/rooms/" + roomId;
    }

    @PostMapping("/rooms/{id}/users/blackuser/add")
    public String join(@PathVariable final String id, @ModelAttribute final JoinUserDTO joinUserDTO, final HttpServletResponse response) {
        int blackUserId = userService.registerUser(joinUserDTO);
        roomService.joinBlackUser(id, blackUserId);
        playerCookie(response, joinUserDTO.getNickname(), joinUserDTO.getPassword(), "black");
        return "redirect:/rooms/" + id;
    }

    @PostMapping("/rooms/{id}/users/blackuser/re-enter")
    public String blackUserReEntry(@PathVariable final String id, @ModelAttribute final PasswordDTO passwordDTO,
                                   final HttpServletResponse response) {
        UserDTO blackUser = roomService.findBlackUserById(id);

        if (!UNKNOWN_USER.equals(blackUser)) {
            userService.checkPassword(Integer.toString(blackUser.getId()), passwordDTO.getPassword());
            playerCookie(response, blackUser.getNickname(), passwordDTO.getPassword(), "black");
            return "redirect:/rooms/" + id;
        }
        return "redirect:/";
    }

    @PostMapping("/rooms/{id}/users/whiteuser/re-enter")
    public String whiteUserReEntry(@PathVariable final String id, @ModelAttribute final PasswordDTO passwordDTO,
                                   final HttpServletResponse response) {
        UserDTO whiteUser = roomService.findWhiteUserById(id);

        if (!UNKNOWN_USER.equals(whiteUser)) {
            userService.checkPassword(Integer.toString(whiteUser.getId()), passwordDTO.getPassword());
            playerCookie(response, whiteUser.getNickname(), passwordDTO.getPassword(), "white");
            return "redirect:/rooms/" + id;
        }
        return "redirect:/";
    }

    private void playerCookie(final HttpServletResponse response, final String nickname, final String password, final String team) {
        Cookie playerId = new Cookie("id", nickname);
        Cookie playerPassword = new Cookie("password", password);
        playerId.setPath("/");
        playerPassword.setPath("/");
        response.addCookie(playerId);
        response.addCookie(playerPassword);
    }

    @GetMapping("/rooms/{id}")
    public String enterRoom(@PathVariable final String id, final Model model) {
        model.addAttribute("state",
                new GameDTO(id, userService.participatedUsers(id), roomService.loadChessGameById(id), "새로운게임"));
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
