package chess.controller.view;

import chess.domain.crypto.EncryptedRoom;
import chess.service.ChessService;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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

/*
    @PostMapping("/games/first")
    public String playNewGame(@RequestParam("room-name") String roomName,
                              @RequestParam("room-password") String roomPassword,
                              Model model) {
        model.addAttribute("isNewGame", true);
        return "game";
    }
*/

    @PostMapping("/games/first")
    public String playNewGame(EncryptedRoom encryptedRoom,
                              Model model) {
        model.addAttribute("isNewGame", true);

        System.out.println("encryptedRoom = " + encryptedRoom);

        return "game";
    }

    @GetMapping("/load-game")
    public String loadGame(HttpSession session, Model model) {

        if (!chessService.isExistGame()) {
            return "redirect:/";
        }

        model.addAttribute("isNewGame", false);

        return "game";
    }
}
