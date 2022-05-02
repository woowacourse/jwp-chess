package chess.application.web.controller;

import chess.application.web.dao.CommandDao;
import chess.application.web.dao.RoomsDao;
import chess.chessboard.position.Position;
import chess.game.Player;
import chess.piece.Piece;
import chess.state.Start;
import chess.state.State;
import chess.state.Status;
import chess.application.web.dto.RoomDto;
import chess.view.Square;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ChessController {
  
    private final CommandDao commandDao;
    private final RoomsDao roomsDao;

    public ChessController(CommandDao commandDao, RoomsDao roomsDao) {
        this.commandDao = commandDao;
        this.roomsDao = roomsDao;
    }

    @GetMapping("/")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("rooms", roomsDao.findAll());
        modelAndView.addObject("roomCount", roomsDao.countRooms());
        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView rooms() {
        ModelAndView modelAndView = new ModelAndView("create");
        return modelAndView;
    }

    @PostMapping("/create")
    public String createRoom(RedirectAttributes redirectAttributes, @RequestParam("name") String name, @RequestParam("password") String password) {
        if (name.isBlank()) {
            return "redirect:create";
        }
        final int roomId = roomsDao.createRoomId();
        roomsDao.insertRoom(roomId, name, password);
        redirectAttributes.addAttribute("roomId", roomId);
        redirectAttributes.addAttribute("message", "게임을 시작합니다.");
        return "redirect:game";
    }

    @GetMapping(path = "/enter")
    public String enterRoom(RedirectAttributes redirectAttributes, @RequestParam("roomId") int roomId) {
        redirectAttributes.addAttribute("roomId", roomId);
        redirectAttributes.addAttribute("message");
        return "redirect:/game";
    }

    @GetMapping(path = "/game")
    public ModelAndView printCurrentBoard(@RequestParam("roomId") int roomId, @RequestParam(value = "message", required = false) String message) {
        State state = currentState(roomId);
        ModelAndView modelAndView = new ModelAndView(getViewName(state));
        modelAndView.addObject("squares", showChessBoard(state.getBoard()));
        modelAndView.addObject("player", playerName(state.getPlayer()));
        modelAndView.addObject("commands", commandDao.findAllById(roomId));
        modelAndView.addObject("message", message);
        modelAndView.addObject("roomId", roomId);
        modelAndView.addObject("name", roomsDao.findNameById(roomId));
        return modelAndView;
    }

    @PostMapping(path = "/game")
    public String movePiece(RedirectAttributes redirectAttributes, @RequestParam("roomId") int roomId, @RequestParam("command") String command) {
        try {
            redirectAttributes.addAttribute("roomId", roomId);
            currentState(roomId).proceed(command);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addAttribute("message", e.getMessage());
            return "redirect:game";
        }
        commandDao.insert(roomId, command);
        redirectAttributes.addAttribute("message", "실행한 명령어: " + command);
        return "redirect:game";
    }

    @PostMapping(path = "/result")
    public String result(RedirectAttributes redirectAttributes, @RequestParam("roomId") int roomId) {
        commandDao.insert(roomId,"status");
        redirectAttributes.addAttribute("roomId", roomId);
        return "redirect:result";
    }

    @GetMapping(path = "/result")
    public ModelAndView printResult(@RequestParam("roomId") int roomId) {
        ModelAndView modelAndView = new ModelAndView("status");
        State state = currentState(roomId);
        Status status = (Status) state;
        HashMap<Player, Double> results = status.calculateScore();
        modelAndView.addObject("roomId", roomId);
        modelAndView.addObject("squares", showChessBoard(state.getBoard()));
        modelAndView.addObject("whiteScore", results.get(Player.WHITE));
        modelAndView.addObject("blackScore", results.get(Player.BLACK));
        return modelAndView;
    }

    @PostMapping(path ="/remove")
    public String removeRoom(@RequestParam int roomId, @RequestParam String password) {
        roomsDao.removeRoom(roomId, password);
        return "redirect:";
    }

    @PostMapping(path = "/check-password")
    public ModelAndView checkPassword(@ModelAttribute RoomDto room) {
        if (currentState(room.getId()).isRunning()) {
            ModelAndView modelAndView = new ModelAndView("index");
            modelAndView.addObject("message", "진행 중인 게임은 삭제할 수 없습니다.");
            modelAndView.addObject("rooms", roomsDao.findAll());
            modelAndView.addObject("roomCount", roomsDao.countRooms());
            return modelAndView;
        }
        ModelAndView modelAndView = new ModelAndView("remove");
        modelAndView.addObject("roomId", room.getId());
        modelAndView.addObject("name", room.getName());
        return modelAndView;
    }

    private State currentState(final int id) {
        List<String> commands = commandDao.findAllById(id);
        State state = Start.of();
        for (String command : commands) {
            state = state.proceed(command);
        }
        return state;
    }

    private String getViewName(State state) {
        if (state.isRunning()) {
            return "game";
        }
        return "finished";
    }

    private List<Square> showChessBoard(final Map<Position, Piece> board) {
        final List<Square> squares = new ArrayList<>();
        for (final Position position : board.keySet()) {
            addPiece(position, board.get(position), squares);
        }
        return squares;
    }

    private void addPiece(final Position position, final Piece piece, final List<Square> squares) {
        if (!piece.isBlank()) {
            squares.add(new Square(piece.getImageName(), position.getPosition()));
        }
    }

    private String playerName(final Player player) {
        return player.getName();
    }
}
