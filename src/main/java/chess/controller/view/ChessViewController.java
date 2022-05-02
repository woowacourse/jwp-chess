package chess.controller.view;

import static chess.util.RandomCreationUtils.createUuid;

import chess.domain.crypto.EncryptedRoom;
import chess.service.ChessService;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ChessViewController {

    private final ChessService chessService;

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
        model.addAttribute("createdRoomId", createUuid());

        return "game";
    }
*/

    @PostMapping("/games/first")
    public String playNewGame(EncryptedRoom encryptedRoom,
                              Model model) {

        model.addAttribute("isNewGame", true);
        model.addAttribute("createdRoomId", createUuid());
        model.addAttribute("roomName", encryptedRoom.getName());
        model.addAttribute("encryptedRoomPassword", encryptedRoom.getEncryptedPassword());

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
