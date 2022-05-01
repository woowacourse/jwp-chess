package chess.controller;

import chess.controller.view.BoardView;
import chess.dto.MoveCommandDto;
import chess.dto.RoomDto;
import chess.service.ChessGameService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/game")
public class ChessGameController {

    private static final String WELCOME_MESSAGE = "어서오세요 :)";
    private static final String MOVE_SUCCESS_MESSAGE = "성공적으로 이동했습니다.";

    private final ChessGameService chessGameService;

    public ChessGameController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView exception(HttpServletRequest request, Exception e) {
        long gameId = Long.parseLong(request.getRequestURI().split("/")[2]);
        return getModelWithGameMessage(e.getMessage(), "redirect:/game/" + gameId);
    }

    @PostMapping(path = "/start", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ModelAndView createGame(@ModelAttribute RoomDto roomDto) {
        long gameId = chessGameService.create(roomDto.getTitle(),
            roomDto.getPassword());

        return getModelWithGameMessage(WELCOME_MESSAGE, "redirect:/game/" + gameId);
    }

    @GetMapping("/{gameId}")
    public ModelAndView getGameByGameId(@PathVariable long gameId,
        @RequestParam(required = false, defaultValue = MOVE_SUCCESS_MESSAGE) String gameMessage) {
        ModelAndView modelAndView = new ModelAndView("game");

        modelAndView.addObject("pieces",
            BoardView.of(chessGameService.findCurrentPieces(gameId)).getBoardView());
        modelAndView.addObject("gameId", gameId);
        modelAndView.addObject("status", chessGameService.calculateGameResult(gameId));
        modelAndView.addObject("gameMessage", gameMessage);

        return modelAndView;
    }

    @PutMapping(path = "/{gameId}/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String move(@PathVariable long gameId, @RequestBody MoveCommandDto MoveCommandDto) {
        chessGameService.move(gameId, MoveCommandDto);
        return "redirect:/game/" + gameId;
    }

    @GetMapping("/")
    public String exitGame() {
        return "redirect:/";
    }

    private ModelAndView getModelWithGameMessage(String message, String url) {
        ModelAndView modelAndView = new ModelAndView(url);
        modelAndView.addObject("gameMessage", message);
        return modelAndView;
    }

}
