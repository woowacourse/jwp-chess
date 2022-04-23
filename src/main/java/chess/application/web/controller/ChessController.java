package chess.application.web.controller;

import chess.application.web.dao.CommandDao;
import chess.chessboard.position.Position;
import chess.game.Player;
import chess.piece.Piece;
import chess.state.Start;
import chess.state.State;
import chess.state.Status;
import chess.view.Square;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ChessController {

    @Autowired
    private CommandDao commandDao;

    @GetMapping("/")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("index");
        return modelAndView;
    }

    @PostMapping("/start")
    public String startChessGame(RedirectAttributes redirectAttributes) {
        commandDao.clear();
        redirectAttributes.addAttribute("message", "게임을 시작합니다.");
        return "redirect:game";
    }

    @GetMapping(path = "/game")
    public ModelAndView printCurrentBoard(@RequestParam("message") String message) {
        State state = currentState();
        ModelAndView modelAndView = new ModelAndView(getViewName(state));
        modelAndView.addObject("squares", showChessBoard(state.getBoard()));
        modelAndView.addObject("player", playerName(state.getPlayer()));
        modelAndView.addObject("commands", commandDao.findAll());
        modelAndView.addObject("message", message);
        return modelAndView;
    }

    @PostMapping(path = "/game")
    public String movePiece(RedirectAttributes redirectAttributes, @RequestParam("command") String command) {
        currentState().proceed(command);
        commandDao.insert(command);
        redirectAttributes.addAttribute("message", "실행한 명령어: " + command);
        return "redirect:game";
    }

    @PostMapping(path = "/result")
    public String result() {
        commandDao.insert("status");
        return "redirect:result";
    }

    @GetMapping(path = "/result")
    public ModelAndView printResult() {
        ModelAndView modelAndView = new ModelAndView("status");
        State state = currentState();
        Status status = (Status) state;
        HashMap<Player, Double> results = status.calculateScore();
        modelAndView.addObject("squares", showChessBoard(state.getBoard()));
        modelAndView.addObject("whiteScore", results.get(Player.WHITE));
        modelAndView.addObject("blackScore", results.get(Player.BLACK));
        return modelAndView;
    }

    private State currentState() {
        List<String> commands = commandDao.findAll();
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
