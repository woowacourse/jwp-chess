package chess.controller;

import chess.controller.dto.response.ChessGameRoomTitleResponse;
import chess.controller.dto.response.PieceResponse;
import chess.service.ChessGameRoomService;
import chess.service.ChessGameService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ChessGameViewController {

    private final ChessGameRoomService chessGameRoomService;
    private final ChessGameService chessGameService;

    public ChessGameViewController(ChessGameRoomService chessGameRoomService, ChessGameService chessGameService) {
        this.chessGameRoomService = chessGameRoomService;
        this.chessGameService = chessGameService;
    }

    @GetMapping("/")
    public ModelAndView mainPage() {
        ModelAndView modelAndView = new ModelAndView("chess_game_list");
        List<ChessGameRoomTitleResponse> chessGameRoomTitleResponses = chessGameRoomService.findAllChessGameRooms()
                .stream()
                .map(ChessGameRoomTitleResponse::from)
                .collect(Collectors.toList());
        modelAndView.addObject("chessGames", chessGameRoomTitleResponses);
        return modelAndView;
    }

    @GetMapping("/chessgames/{chessGameId}")
    public ModelAndView loadChessGame(@PathVariable long chessGameId) {
        ModelAndView modelAndView = new ModelAndView("chess_game");
        List<PieceResponse> pieceResponses = chessGameService.findChessBoard(chessGameId)
                .entrySet()
                .stream()
                .map(PieceResponse::from)
                .collect(Collectors.toList());
        modelAndView.addObject("pieces", pieceResponses);
        return modelAndView;
    }
}
