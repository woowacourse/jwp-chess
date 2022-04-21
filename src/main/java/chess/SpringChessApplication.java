package chess;

import chess.domain.game.ChessController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@Controller
public class SpringChessApplication {

    private final ChessController controller = new ChessController();

    public static void main(String[] args) {
        SpringApplication.run(SpringChessApplication.class, args);
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("boards", controller.getRooms());
        return "home";
    }
}
