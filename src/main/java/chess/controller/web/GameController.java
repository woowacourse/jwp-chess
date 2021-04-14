package chess.controller.web;

import chess.controller.dto.WinnerDto;
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
    private void createGame(@PathVariable final Long roomId, HttpServletResponse httpServletResponse) throws IOException {
        gameService.create(roomId);
        httpServletResponse.sendRedirect("/game/load/" + roomId);
    }

    @GetMapping("/game/delete/{roomId}")
    private void deleteGame(@PathVariable final Long roomId, HttpServletResponse httpServletResponse) throws IOException {
        gameService.delete(roomId);
        httpServletResponse.sendRedirect("/main");
    }

    @GetMapping("/game/load/{roomId}")
    private String loadGame(@PathVariable final Long roomId, Model model) throws SQLException {
        return printGame(roomId, model);
    }

    @ResponseBody
    @PostMapping("/game/show/{roomId}")
    private String show(@PathVariable final Long roomId, HttpServletRequest httpServletRequest) {
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
            return printWinnerResult(roomId, model);
        }

        model.addAttribute("room", roomService.room(roomId));
        model.addAttribute("board", gameService.board(roomId));
        model.addAttribute("scores", gameService.scores(roomId));
        return "chessBoard";
    }

    private String printWinnerResult(Long roomId, Model model) {
        final List<Owner> winner = gameService.winner(roomId);
        roomService.delete(roomId);
        gameService.delete(roomId);

        final WinnerDto winnerDto = new WinnerDto(OutputView.decideWinnerName(winner));
        model.addAttribute("winner", winnerDto);
        return "result";
    }
}