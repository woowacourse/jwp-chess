package chess.controller;

import static chess.controller.ChessGameController.getModelWithGameMessage;

import chess.domain.room.Room;
import chess.dto.RoomDto;
import chess.service.ChessGameService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        return getModelAndView(e.getMessage());
    }

    @GetMapping("/")
    public ModelAndView getRooms(@RequestParam(required = false, defaultValue = WELCOME_MESSAGE) String gameMessage) {
        return getModelAndView(gameMessage);
    }

    @DeleteMapping("/game")
    public String deleteGame(@ModelAttribute RoomDto roomDto) {
        chessGameService.cleanGameByIdAndPassword(roomDto.getId(), roomDto.getPassword());
        return "redirect:/";
    }

    //TODO room수정
    @PostMapping(path = "/game", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ModelAndView createGame(@ModelAttribute RoomDto roomDto) {
        long gameId = chessGameService.create(new Room(roomDto.getTitle(), roomDto.getPassword()));

        return getModelWithGameMessage(WELCOME_MESSAGE, "redirect:/game/" + gameId);
    }

    private ModelAndView getModelAndView(String message) {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("gameMessage", message);
        modelAndView.addObject("rooms", chessGameService.getAllGames());
        return modelAndView;
    }

}
