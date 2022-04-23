package chess.controller;

import chess.dto.MoveCommandDto;
import chess.service.ChessGameService;
import chess.web.view.BoardView;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SpringChessController {

    public static final String MOVE_SUCCESS_MESSAGE = "성공적으로 이동했습니다";

    ChessGameService chessGameService = new ChessGameService();
    String error = "";

    @ExceptionHandler(Exception.class)
    public ModelAndView exception(String gameId, Exception e) {
        error = e.getMessage();
        return getModelUrl("redirect:/game", "1234");
    }

    @GetMapping("/game/exit")
    private String postExit(@RequestParam String gameId) {
        chessGameService.cleanGame(gameId);
        return "redirect:/";
    }

    @PostMapping("/game/move")
    private ModelAndView postMove(@RequestBody MoveCommandDto moveDto) {
        try {
            chessGameService.move(moveDto.getGameId(), moveDto);
            error = MOVE_SUCCESS_MESSAGE;
        } catch (Exception e) {
            error = e.getMessage();
        }
        return getModelUrl("redirect:/game" , moveDto.getGameId());
    }

    @GetMapping("/game")
    private ModelAndView getGamePage(@RequestParam String gameId) {
        return getModel(gameId);
    }

    @GetMapping("/game/start")
    public String getStartPage(@RequestParam String gameId) {
        chessGameService.createOrGet(gameId);
        return "redirect:/game?gameId=" + gameId;
    }

    private ModelAndView getModel(String gameId) {
        ModelAndView modelAndView = new ModelAndView("game");
        modelAndView.addObject("pieces", BoardView.of(chessGameService.getCurrentGame(gameId)).getBoardView());
        modelAndView.addObject("gameId", gameId);
        modelAndView.addObject("status", chessGameService.calculateGameResult(gameId));
        modelAndView.addObject("gameMessage", error);
        return modelAndView;
    }

    private ModelAndView getModelUrl(String url, String gameId) {
        ModelAndView modelAndView = new ModelAndView(url);
        modelAndView.addObject("pieces", BoardView.of(chessGameService.getCurrentGame(gameId)).getBoardView());
        modelAndView.addObject("gameId", gameId);
        modelAndView.addObject("status", chessGameService.calculateGameResult(gameId));
        return modelAndView;
    }
}
