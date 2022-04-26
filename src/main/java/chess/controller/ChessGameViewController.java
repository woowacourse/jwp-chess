package chess.controller;

import chess.controller.dto.response.ChessGameRoomTitleResponse;
import chess.service.ChessGameRoomService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ChessGameViewController {

    private final ChessGameRoomService chessGameRoomService;

    public ChessGameViewController(final ChessGameRoomService chessGameRoomService) {
        this.chessGameRoomService = chessGameRoomService;
    }

    @GetMapping("/")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("chess_game_list");
        List<ChessGameRoomTitleResponse> chessGameRoomTitleResponses = chessGameRoomService.findAllChessGameRooms()
                .stream()
                .map(ChessGameRoomTitleResponse::from)
                .collect(Collectors.toList());
        modelAndView.addObject("chessGames", chessGameRoomTitleResponses);
        return modelAndView;
    }
}
