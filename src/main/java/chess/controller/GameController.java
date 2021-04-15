package chess.controller;

import chess.domain.board.position.Position;
import chess.domain.piece.Owner;
import chess.service.GameService;
import chess.service.RoomService;
import chess.view.OutputView;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Controller
public class GameController {
    private final RoomService roomService;
    private final GameService gameService;

    public GameController(RoomService roomService, GameService gameService) {
        this.roomService = roomService;
        this.gameService = gameService;
    }

    @GetMapping("/game/create/{roomId}")
    private void createGame(@PathVariable final Long roomId, final HttpServletResponse response) throws IOException {
        gameService.create(roomId);
        response.sendRedirect("/game/load/" + roomId);
    }

    @GetMapping("/game/delete/{roomId}")
    private void deleteGame(@PathVariable final Long roomId, final HttpServletResponse response) throws IOException {
        gameService.delete(roomId);
        response.sendRedirect("/main");
    }

    @GetMapping("/game/load/{roomId}")
    private String loadGame(@PathVariable final Long roomId, final Model model) throws SQLException {
        return printGame(roomId, model);
    }

    @ResponseBody
    @PostMapping("/game/show/{roomId}")
    private String show(@PathVariable final Long roomId, final HttpServletRequest request) {
        final Position source = new Position(request.getParameter("source"));
        return gameService.show(roomId, source).toString();
    }

    @PostMapping("/game/move/{roomId}")
    private String move(@PathVariable final Long roomId, final HttpServletRequest request, final Model model) throws SQLException {
        Position source = new Position(request.getParameter("source"));
        Position target = new Position(request.getParameter("target"));
        gameService.move(roomId, source, target);
        return printGame(roomId, model);
    }

    private String printGame(final Long roomId, final Model model) throws SQLException {
        if (gameService.isGameEnd(roomId)) {
            final List<Owner> winner = gameService.winner(roomId);
            roomService.delete(roomId);
            gameService.delete(roomId);

            model.addAttribute("winner", OutputView.decideWinnerName(winner));
            return "result";
        }

        model.addAttribute("room", roomService.room(roomId));
        model.addAttribute("board", gameService.board(roomId));
        model.addAttribute("scores", gameService.scores(roomId));
        return "chessBoard";
    }
}