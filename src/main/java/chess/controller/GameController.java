package chess.controller;

import chess.dto.ChessGameDto;
import chess.service.ChessGameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class GameController {

    private static final int COLUMN_INDEX = 0;
    private static final int ROW_INDEX = 1;

    private final ChessGameService chessGameService;

    public GameController(final ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping("/games/{chessGameId}")
    public String getChessGamePage(final Model model, final @PathVariable long chessGameId) {
        ChessGameDto chessGameDto = chessGameService.loadChessGame(chessGameId);
        if (chessGameDto.isOn()) {
            model.addAllAttributes(chessGameDto.getBoardForHtml());
        }
        model.addAttribute("id", chessGameId);
        model.addAttribute("chess_game_name", chessGameDto.getName());
        model.addAttribute("turn", chessGameDto.getTurn());
        model.addAttribute("result", chessGameDto.getResult());
        return "chess_game";
    }

    @PostMapping("/games/move/{chessGameId}")
    public RedirectView movePiece(final @PathVariable long chessGameId,
                                  final @RequestParam String source,
                                  final @RequestParam String target) {
        String refinedSource = source.trim().toLowerCase();
        String refinedTarget = target.trim().toLowerCase();
        chessGameService.movePiece(
                chessGameId,
                refinedSource.charAt(COLUMN_INDEX),
                Character.getNumericValue(refinedSource.charAt(ROW_INDEX)),
                refinedTarget.charAt(COLUMN_INDEX),
                Character.getNumericValue(refinedTarget.charAt(ROW_INDEX))
        );
        return new RedirectView("/games/" + chessGameId);
    }

    @PostMapping("/games/reset/{chessGameId}")
    public RedirectView resetChessGame(final @PathVariable long chessGameId) {
        chessGameService.resetChessGame(chessGameId);
        return new RedirectView("/games/" + chessGameId);
    }

    @PostMapping("/games/end/{chessGameId}")
    public RedirectView endChessGame(final @PathVariable long chessGameId) {
        chessGameService.endChessGame(chessGameId);
        return new RedirectView("/games/" + chessGameId);
    }

}
