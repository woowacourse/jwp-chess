package chess.controller;

import chess.domain.board.position.Position;
import chess.domain.piece.Owner;
import chess.service.GameService;
import chess.service.RoomService;
import chess.view.OutputView;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("/game")
public class GameController {
    private final RoomService roomService;
    private final GameService gameService;

    public GameController(final RoomService roomService, final GameService gameService) {
        this.roomService = roomService;
        this.gameService = gameService;
    }

    @GetMapping("/create/{roomId}")
    private String createGame(@PathVariable final Long roomId) {
        gameService.create(roomId);
        return "redirect:/game/load/" + roomId;
    }

    @GetMapping("/delete/{roomId}")
    private String deleteGame(@PathVariable final Long roomId) {
        gameService.delete(roomId);
        return "redirect:/room/list";
    }

    @ResponseBody
    @GetMapping("/show/{roomId}")
    private String show(@PathVariable final Long roomId, @RequestParam final Position source) {
        return gameService.show(roomId, source).toString();
    }

    @PostMapping("/move/{roomId}")
    private String move(@PathVariable final Long roomId, final HttpServletRequest request, final Model model) throws SQLException {
        Position source = new Position(request.getParameter("source"));
        Position target = new Position(request.getParameter("target"));
        gameService.move(roomId, source, target);
        return printResult(roomId, model);
    }

    @GetMapping("/load/{roomId}")
    private String loadGame(@PathVariable final Long roomId, final Model model) throws SQLException {
        return printResult(roomId, model);
    }

    private String printResult(final Long roomId, final Model model) throws SQLException {
        if (gameService.isGameEnd(roomId)) {
            final List<Owner> winner = gameService.winner(roomId);
            roomService.delete(roomId);
            gameService.delete(roomId);
            return OutputView.printWinningResult(model, winner);
        }
        return OutputView.printBoardStatus(model, roomService.roomInfo(roomId), gameService.gameInfo(roomId));
    }
}