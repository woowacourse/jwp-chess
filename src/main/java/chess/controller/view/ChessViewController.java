package chess.controller.view;

import chess.exception.ClientException;
import chess.service.ChessService;
import javax.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class ChessViewController {

    private final ChessService chessService;

    public ChessViewController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String root() {
        return "index";
    }

    @GetMapping("/index")
    public String index() {
        return "redirect:/";
    }

    @GetMapping("/new-game")
    public String playNewGame(Model model) {
        model.addAttribute("isNewGame", true);
        return "game";
    }

    @GetMapping("/load-game")
    public String loadGame(HttpSession session, Model model) {

        if (!chessService.isExistGame()) {
            return "redirect:/";
        }

        model.addAttribute("isNewGame", false);
        chessService.loadLastGame(session);

        return "game";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public String handleClientException(ClientException exception) {
        exception.printStackTrace();
        return exception.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public String handleException(Exception exception) throws Exception {
        exception.printStackTrace();
        return exception.getMessage();
    }
}
