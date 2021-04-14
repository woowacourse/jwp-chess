package chess.controller.web;

import chess.controller.dto.BoardDto;
import chess.controller.dto.RoomDto;
import chess.controller.dto.ScoresDto;
import chess.controller.dto.WinnerDto;
import chess.domain.board.position.Position;
import chess.domain.piece.Owner;
import chess.service.GameService;
import chess.service.RequestHandler;
import chess.service.RoomService;
import chess.view.OutputView;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;

import static spark.Spark.get;
import static spark.Spark.post;

@Controller
public class GameController {
    private static int SIZE_OF_ONLY_WINNER = 1;

    private final RoomService roomService;
    private final GameService gameService;

    public GameController(RoomService roomService, GameService gameService) {
        this.roomService = roomService;
        this.gameService = gameService;
    }

    @GetMapping("/game/create/{roomId}")
    private void createGame(@PathVariable final Long roomId, HttpServletResponse httpServletResponse) throws SQLException, IOException {
        gameService.create(roomId);
        httpServletResponse.sendRedirect("/game/load/" + roomId);
    }

    @GetMapping("/game/delete/{roomId}")
    private void deleteGame(@PathVariable final Long roomId, HttpServletResponse httpServletResponse) throws SQLException, IOException {
        gameService.delete(roomId);
        httpServletResponse.sendRedirect("/main");
    }

    @GetMapping("/game/load/{roomId}")
    private String loadGame(@PathVariable final Long roomId, Model model) throws SQLException {
        return printGame(roomId, model);
    }

    @ResponseBody
    @PostMapping("/game/show/{roomId}")
    private String show(@PathVariable final Long roomId, HttpServletRequest httpServletRequest) throws SQLException {
        try{
           Position source = new Position(httpServletRequest.getParameter("source"));
           return gameService.show(roomId, source).toString();
        }catch (Exception e){
            return null;
        }
    }

    @PostMapping("/game/move/{roomId}")
    private String move(Model model, @PathVariable final Long roomId, HttpServletRequest httpServletRequest) throws SQLException {
        Position source = new Position(httpServletRequest.getParameter("source"));
        Position target = new Position(httpServletRequest.getParameter("target"));
        gameService.move(roomId, source, target);
        return printGame(roomId, model);
    }

    private String printGame(final Long roomId, final Model model) throws SQLException {
        if (gameService.isGameEnd(roomId)) {
            final List<Owner> winner = gameService.winner(roomId);
            roomService.delete(roomId);
            gameService.delete(roomId);

            final WinnerDto winnerDto = new WinnerDto(decideWinnerName(winner));
            model.addAttribute("winner", winnerDto);
            return "result";
        }

        model.addAttribute("room", roomService.room(roomId));
        model.addAttribute("board", gameService.board(roomId));
        model.addAttribute("scores", gameService.scores(roomId));
        return "chessBoard";
    }

    private static String decideWinnerName(final List<Owner> winners) {
        if (winners.size() == SIZE_OF_ONLY_WINNER) {
            final Owner winner = winners.get(0);
            return winner.name();
        }
        return "무승부";
    }
}