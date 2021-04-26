package chess.controller;

import chess.domain.ChessGame;
import chess.domain.Team;
import chess.dto.CreateRoomRequestDTO;
import chess.dto.EnterRoomRequestDTO;
import chess.dto.PiecesDTO;
import chess.dto.RoomIdDTO;
import chess.dto.UsersDTO;
import chess.service.HistoryService;
import chess.service.ResultService;
import chess.service.RoomService;
import chess.service.UserService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/chess")
public final class SpringChessGameController {

    private final RoomService roomService;
    private final ResultService resultService;
    private final UserService userService;
    private final HistoryService historyService;

    public SpringChessGameController(final RoomService roomService,
        final ResultService resultService,
        final UserService userService, final HistoryService historyService) {
        this.roomService = roomService;
        this.resultService = resultService;
        this.userService = userService;
        this.historyService = historyService;
    }

    @GetMapping()
    public String goHome(final Model model) {
        try {
            roomService.loadAllRooms();
            model.addAttribute("rooms", roomService.allRooms());
            model.addAttribute("results", resultService.allUserResult());
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "index";
    }

    @PostMapping(path = "/new-game")
    public String createNewGame(@ModelAttribute final CreateRoomRequestDTO requestDTO) {
        userService.enrollUser(requestDTO.getNickname(), requestDTO.getPassword());
        int whiteUserId = userService.userIdByNickname(requestDTO.getNickname());
        roomService.createRoom(requestDTO.getTitle(), whiteUserId);
        return "redirect:room/" + roomService.createdRoomId();
    }

    @PostMapping("/enter-black")
    public String enterBlack(@ModelAttribute final EnterRoomRequestDTO requestDTO) {
        userService.enrollUser(requestDTO.getNickname(), requestDTO.getPassword());
        int blackUserId = userService.userIdByNickname(requestDTO.getNickname());
        roomService.enrollBlackUser(requestDTO.getId(), blackUserId);
        return "redirect:room/" + requestDTO.getId();
    }

    @PostMapping("/enter-white")
    public String enterWhite(@ModelAttribute final EnterRoomRequestDTO requestDTO) {
        userService.enrollUser(requestDTO.getNickname(), requestDTO.getPassword());
        int whiteUserId = userService.userIdByNickname(requestDTO.getNickname());
        roomService.enrollWhiteUser(requestDTO.getId(), whiteUserId);
        return "redirect:room/" + requestDTO.getId();
    }

    @GetMapping(path = "/room/{roomId}")
    public String enterRoom(@PathVariable final String roomId, final Model model) {
        try {
            model.addAttribute("roomId", roomId);
            model.addAttribute("button", "새로운게임");
            model.addAttribute("isWhite", true);
            model.addAttribute("users", userService.usersParticipatedInGame(roomId));
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "chess";
    }

    @PostMapping(path = "/start")
    public String startGame(@ModelAttribute final RoomIdDTO roomIdDTO, final Model model) {
        ChessGame chessGame = new ChessGame();
        chessGame.initialize();
        String roomId = roomIdDTO.getRoomId();

        roomService.addRoom(roomId, chessGame);
        historyService.initializeByRoomId(roomId);

        UsersDTO users = userService.usersParticipatedInGame(roomId);
        gameInformation(chessGame, model, roomId, users);
        return "chess";
    }

    @PostMapping(path = "/continue")
    public String continueGame(@ModelAttribute final RoomIdDTO roomIdDTO, final Model model) {
        String roomId = roomIdDTO.getRoomId();
        ChessGame chessGame = roomService.loadGameByRoomId(roomId);
        chessGame.initialize();

        List<String[]> logs = historyService.logByRoomId(roomId);
        historyService.executeLog(logs, chessGame);

        UsersDTO users = userService.usersParticipatedInGame(roomId);
        gameInformation(roomService.loadGameByRoomId(roomId), model, roomId, users);
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
        model.addAttribute("roomId", roomId);
        model.addAttribute("users", users);
    }

    @GetMapping(path = "/error-page")
    public String errorPage(@RequestParam final String error, final Model model) {
        model.addAttribute("error", error);
        return "error";
    }
}
