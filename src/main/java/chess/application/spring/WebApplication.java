package chess.application.spring;

import chess.chessboard.position.Position;
import chess.game.Player;
import chess.piece.Piece;
import chess.state.Start;
import chess.state.State;
import chess.view.Square;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@Controller
public class WebApplication {

    private State state;

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

    @GetMapping("/")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("index");
        return modelAndView;
    }

    @PostMapping("/start")
    public String startChessGame() {
        state = Start.of();
        return "redirect:start";
    }

    @GetMapping(path = "/start")
    public ModelAndView printInitBoard() {
        ModelAndView modelAndView = new ModelAndView("game");
        modelAndView.addObject("squares", showChessBoard(state.getBoard()));
        modelAndView.addObject("player", playerName(state.getPlayer()));
        modelAndView.addObject("message", "게임을 시작합니다.");
        return modelAndView;
    }

    @GetMapping(path = "/game")
    public ModelAndView printCurrentBoard() {
        ModelAndView modelAndView = new ModelAndView("game");
        modelAndView.addObject("squares", showChessBoard(state.getBoard()));
        modelAndView.addObject("player", playerName(state.getPlayer()));
        return modelAndView;
    }

    @PostMapping(path = "/game")
    public String movePiece(@RequestParam("command") String requestBody) {
        state = state.proceed(requestBody);
        return "redirect:game";
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
