package chess.controller;

import chess.domain.exception.DataException;
import chess.service.ChessService;
import chess.view.ModelView;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/play")
public class ChessController {

    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("")
    public String play(Model model) throws DataException {
        model.addAllAttributes(ModelView.historyResponse(chessService.loadHistory()));
        return "lobby";
    }

    @GetMapping("/{id}")
    public String play(Model model, @PathVariable String id) throws DataException {
        model.addAllAttributes(ModelView.gameResponse(
                chessService.initialGameInfo(),
                id
        ));
        return "chessGame";
    }

    @GetMapping("/continue/{id}")
    public String continueGame(Model model, @PathVariable String id) throws DataException {
        model.addAllAttributes(ModelView.gameResponse(chessService.continuedGameInfo(id), id));
        return "chessGame";
    }

    @GetMapping("/end")
    public String endGame() {
        return "lobby";
    }
}
