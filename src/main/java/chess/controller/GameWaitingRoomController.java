package chess.controller;

import chess.dto.GameRoomDto;
import chess.service.ChessGameService;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class GameWaitingRoomController {

    private static final String WELCOME_MESSAGE = "어서오세요 :)";

    private final ChessGameService chessGameService;

    public GameWaitingRoomController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView exception(Exception e) {
        return getModelWithGameMessage(e.getMessage(), "redirect:/");
    }

    @GetMapping("/")
    public ModelAndView getRooms(HttpServletRequest request) {
        return getModel(request);
    }

    @DeleteMapping("/delete")
    public String deleteGame(@ModelAttribute GameRoomDto gameRoomDto) {
        chessGameService.cleanGameByIdAndPassword(gameRoomDto.getId(), gameRoomDto.getPassword());
        return "redirect:/";
    }

    private ModelAndView getModelWithGameMessage(String message, String url) {
        ModelAndView modelAndView = new ModelAndView(url);
        modelAndView.addObject("gameMessage", message);
        return modelAndView;
    }

    private ModelAndView getModel(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("gameMessage", getGameMessage(request));
        modelAndView.addObject("rooms", chessGameService.getAllGames());
        return modelAndView;
    }

    private String getGameMessage(HttpServletRequest request) {
        if (request.getQueryString() == null) {
            return WELCOME_MESSAGE;
        }
        String decodedQueryString = URLDecoder.decode(request.getQueryString(), StandardCharsets.UTF_8);
        return decodedQueryString.split("gameMessage=")[1];
    }
}
