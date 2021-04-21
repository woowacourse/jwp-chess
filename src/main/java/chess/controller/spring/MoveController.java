package chess.controller.spring;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
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

    @GetMapping("/chessgame/{id}")
    public String moveToGamePage(@PathVariable String id, Model model, HttpSession httpSession) {
        String roomId = (String) httpSession.getAttribute("roomId");
        String password = (String) httpSession.getAttribute("password");
        model.addAttribute("roomId", id);
        if (Objects.isNull(roomId) && Objects.isNull(password)) {
            return "login";
        }
        return "game";
    }

    @GetMapping("/result/{id}")
    public String moveToResultPage(@PathVariable String id, Model model) {
        model.addAttribute("roomId", id);
        return "result";
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleException(RuntimeException runtimeException) {
        return ResponseEntity.badRequest().body(runtimeException.getMessage());
    }
}
