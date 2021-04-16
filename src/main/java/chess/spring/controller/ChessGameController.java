package chess.spring.controller;


import chess.spring.controller.dto.response.ChessGameDtoNew;
import chess.spring.service.ChessGameService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class ChessGameController {

    private final ChessGameService chessGameService;

    public ChessGameController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<ChessGameDtoNew> allChessGames = chessGameService.getAllGames();
        model.addAttribute("allChessGames", allChessGames);
        return "index";
    }

    @PostMapping("/games")
    public String createChessGame(@RequestParam String roomTitle) {
        Long createdChessGameId = chessGameService.createNewChessGame(roomTitle);
        return "redirect:/games?id=" + createdChessGameId;
    }

//    @GetMapping("/rooms")
//    public String getChessBoardRequest(@RequestParam("id") Long gameId, Model model) throws SQLException {
//        ResponseDTO responseDTO = chessWebService.getGameStatus(gameId);
//        model.addAttribute("responseDTO", responseDTO);
//        putBoardRanksToModel(model, responseDTO.getBoardResponseDTO());
//        return "chess-board";
//    }
//
//    @GetMapping("/delete")
//    public String deleteRequest(@RequestParam("id") Long gameId) throws SQLException {
//        chessWebService.deleteGame(gameId);
//        return "redirect:/";
//    }
}
