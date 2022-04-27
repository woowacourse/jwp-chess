package chess.controller;

import chess.dto.GameRoomDto;
import chess.service.ChessGameService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class GameWaitingRoomController {

    private final ChessGameService chessGameService;

    public GameWaitingRoomController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping("/")
    public ModelAndView getRooms() {
        List<GameRoomDto> rooms = chessGameService.getAllGames();

        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("rooms", rooms);

        return modelAndView;
    }

}
