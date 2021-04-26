package chess.controller.spring;

import chess.controller.spring.vo.SessionVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;
import java.util.Objects;

@Controller
public class MoveController {

    @GetMapping("/")
    public String moveToMainPage() {
        return "index";
    }

    @GetMapping("/chessgame/{roomId}")
    public String moveToGamePage(@PathVariable int roomId, Model model, HttpSession httpSession) {
        SessionVO session = (SessionVO) httpSession.getAttribute("session");
        model.addAttribute("roomId", roomId);
        if (Objects.isNull(session)) {
            return "login";
        }
        if (roomId != session.getRoomId()) {
            throw new IllegalStateException("현재 플레이 중인 게임이 있습니다.");
        }
        return "game";
    }

    @GetMapping("/result/{roomId}")
    public String moveToResultPage(@PathVariable int roomId, Model model) {
        model.addAttribute("roomId", roomId);
        return "result";
    }
}
