package chess.web.controller;


import chess.web.controller.dto.response.ChessGameResponseDto;
import chess.web.controller.dto.response.GameStatusResponseDto;
import chess.web.service.ChessGameService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
        List<ChessGameResponseDto> notFulledChessGames = chessGameService.getNotFulledChessGames();
        model.addAttribute("notFulledChessGames", notFulledChessGames);
        return "index";
    }

    @PostMapping("/games")
    public String createChessGame(@RequestParam String gameTitle) {
        Long createdChessGameId = chessGameService.createNewChessGame(gameTitle);
        return "redirect:/games/" + createdChessGameId;
    }

    @GetMapping("/games/{gameId}")
    public String chessBoard(@PathVariable Long gameId, Model model) {
        GameStatusResponseDto gameStatusDto = chessGameService.getGameStatus(gameId);
        model.addAttribute("gameStatusDto", gameStatusDto);
        return "chess-game";
    }
}
