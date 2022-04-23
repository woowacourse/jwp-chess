package chess.controller;

import chess.dto.MoveCommandDto;
import chess.service.ChessGameService;
import chess.web.view.BoardView;
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

    public static final String MOVE_SUCCESS_MESSAGE = "성공적으로 이동했습니다";

    ChessGameService chessGameService = new ChessGameService();

    @ExceptionHandler(Exception.class)
    public ModelAndView exception(HttpServletRequest request, Exception e) {
        String gameId = request.getRequestURI().split("/")[2];
        System.out.println(e.getMessage());
        System.out.println("erer"+getModelErrorUrl(e.getMessage(),"redirect:/game/" + gameId, gameId).getModel());
        return getModelErrorUrl(e.getMessage(),"redirect:/game/" + gameId, gameId);
    }

    @GetMapping("/game/{gameId}/exit")
    private String postExit(@PathVariable String gameId) {
        chessGameService.cleanGame(gameId);
        return "redirect:/";
    }

    @PostMapping(path = "/game/{gameId}/move")
    private ModelAndView postMove(@PathVariable String gameId, @RequestBody MoveCommandDto moveDto) {
        chessGameService.move(gameId, moveDto);
        return getModelErrorUrl(MOVE_SUCCESS_MESSAGE,"redirect:/game/" + gameId, gameId);
    }

    @GetMapping("/game/{gameId}")
    private ModelAndView getStartGamePage(@PathVariable String gameId) {
        return getModel(gameId);
    }

    @GetMapping("/game/start")
    public String getStartPage(@RequestParam String gameId) {
        chessGameService.createOrGet(gameId);
        return "redirect:/game/" + gameId;
    }

    private ModelAndView getModelErrorUrl(String errorMessage, String url, String gameId) {
        ModelAndView modelAndView = new ModelAndView(url);
        System.out.println("첫번째 에러 뷰");
        modelAndView.addObject("pieces", BoardView.of(chessGameService.getCurrentGame(gameId)).getBoardView());
        modelAndView.addObject("gameId", gameId);
        modelAndView.addObject("status", chessGameService.calculateGameResult(gameId));
        modelAndView.addObject("gameMessage", errorMessage);
        return modelAndView;
    }

    private ModelAndView getModel(String gameId) {
        ModelAndView modelAndView = new ModelAndView("game");
        System.out.println("두번째 에러 뷰");
        modelAndView.addObject("pieces", BoardView.of(chessGameService.getCurrentGame(gameId)).getBoardView());
        modelAndView.addObject("gameId", gameId);
        modelAndView.addObject("status", chessGameService.calculateGameResult(gameId));
        modelAndView.addObject("gameMessage", "none");
        return modelAndView;
    }
}
