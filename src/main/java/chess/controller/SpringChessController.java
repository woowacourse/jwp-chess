package chess.controller;

import chess.controller.view.BoardView;
import chess.dto.GameRoomDto;
import chess.dto.MoveCommandDto;
import chess.service.ChessGameService;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/game")
public class SpringChessController {

    private static final String WELCOME_MESSAGE = "어서오세요 :)";
    private static final String MOVE_SUCCESS_MESSAGE = "성공적으로 이동했습니다.";

    private final ChessGameService chessGameService;

    public SpringChessController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView exception(HttpServletRequest request, Exception e) {
        String gameId = request.getRequestURI().split("/")[2];
        return getModelWithGameMessage(e.getMessage(), "redirect:/game/" + gameId);
    }

//    @GetMapping("/start")
//    public ModelAndView startGame(@RequestParam String gameId) {
//        //return 값이 있어야 한다.
//        long gameId = chessGameService.create();
//        chessGameService.createOrGet(gameId);
//        return getModelWithGameMessage(WELCOME_MESSAGE, "redirect:/game/" + gameId);
//    }

    @PostMapping(path = "/start", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ModelAndView createGame(GameRoomDto gameRoomDto) {
        long gameId = chessGameService.create(gameRoomDto.getTitle(),
            gameRoomDto.getPassword());
        return getModelWithGameMessage(WELCOME_MESSAGE, "redirect:" + gameId);
    }

    @GetMapping("/start")
    public ModelAndView getGame(@RequestParam String gameId) {
        //return 값이 있어야 한다.
//        long gameId = chessGameService.create();
//        chessGameService.createOrGet(gameId);
        return getModelWithGameMessage(WELCOME_MESSAGE, "redirect:" + gameId);
    }

    @GetMapping("/{gameId}")
    public ModelAndView getGameByGameId(HttpServletRequest request, @PathVariable String gameId) {
        return getModel(request, gameId);
    }

    @PostMapping(path = "/{gameId}/move")
    public ModelAndView move(@PathVariable String gameId, @RequestBody MoveCommandDto MoveCommandDto) {
        //parseLong부분 고치기
        chessGameService.move(Long.parseLong(gameId), MoveCommandDto);
        return getModelWithGameMessage(MOVE_SUCCESS_MESSAGE, "redirect:" + gameId);
    }

    @DeleteMapping("/{gameId}/exit")
    public String exitAndDeleteGame(@PathVariable String gameId) {
        chessGameService.cleanGame(Long.parseLong(gameId));
        return "redirect:/";
    }

    private ModelAndView getModelWithGameMessage(String massage, String url) {
        ModelAndView modelAndView = new ModelAndView(url);
        modelAndView.addObject("gameMessage", massage);
        return modelAndView;
    }

    private ModelAndView getModel(HttpServletRequest request, String gameId) {
        ModelAndView modelAndView = new ModelAndView("game");
        modelAndView.addObject("pieces", BoardView.of(chessGameService.getCurrentGame(Long.parseLong(gameId))).getBoardView());
        modelAndView.addObject("gameId", gameId);
        modelAndView.addObject("status", chessGameService.calculateGameResult(Long.parseLong(gameId)));
        modelAndView.addObject("gameMessage", getGameMessage(request));
        return modelAndView;
    }

    private String getGameMessage(HttpServletRequest request) {
        String decodedQueryString = URLDecoder.decode(request.getQueryString(), StandardCharsets.UTF_8);
        return decodedQueryString.split("gameMessage=")[1];
    }

}
