package chess.controller;

import chess.controller.view.BoardView;
import chess.dao.GameDao;
import chess.dao.PieceDao;
import chess.dto.MoveCommandDto;
import chess.service.ChessGameService;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SpringChessController {

    private static final String MOVE_SUCCESS_MESSAGE = "성공적으로 이동했습니다";

    private final ChessGameService chessGameService;

    public SpringChessController(GameDao gameDao, PieceDao pieceDao) {
        this.chessGameService = new ChessGameService(pieceDao, gameDao);
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView exception(HttpServletRequest request, Exception e) {
        String gameId = request.getRequestURI().split("/")[2];
        return getModelWithGameMessage(e.getMessage(), "redirect:/game/" + gameId);
    }

    @GetMapping("/game/start")
    public String startGame(@RequestParam String gameId) {
        chessGameService.createOrGet(gameId);
        return "redirect:/game/" + gameId;
    }

    @GetMapping("/game/{gameId}")
    public ModelAndView getGameByGameId(HttpServletRequest request, @PathVariable String gameId) {
        return getModel(request, gameId);
    }

    @PostMapping(path = "/game/{gameId}/move")
    public ModelAndView move(@PathVariable String gameId, @RequestBody MoveCommandDto MoveCommandDto) {
        chessGameService.move(gameId, MoveCommandDto);
        return getModelWithGameMessage(MOVE_SUCCESS_MESSAGE, "redirect:/game/" + gameId);
    }

    @GetMapping("/game/{gameId}/exit")
    public String exitAndDeleteGame(@PathVariable String gameId) {
        chessGameService.cleanGame(gameId);
        return "redirect:/";
    }

    private ModelAndView getModelWithGameMessage(String errorMessage, String url) {
        ModelAndView modelAndView = new ModelAndView(url);
        modelAndView.addObject("gameMessage", errorMessage);
        return modelAndView;
    }

    private ModelAndView getModel(HttpServletRequest request, String gameId) {
        ModelAndView modelAndView = new ModelAndView("game");
        modelAndView.addObject("pieces", BoardView.of(chessGameService.getCurrentGame(gameId)).getBoardView());
        modelAndView.addObject("gameId", gameId);
        modelAndView.addObject("status", chessGameService.calculateGameResult(gameId));
        modelAndView.addObject("gameMessage", getGameMessage(request));
        return modelAndView;
    }

    private String getGameMessage(HttpServletRequest request) {
        String decodedQueryString = URLDecoder.decode(request.getQueryString(), StandardCharsets.UTF_8);
        return decodedQueryString.split("gameMessage=")[1];
    }

}
