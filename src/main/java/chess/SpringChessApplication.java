package chess;

import chess.domain.game.ChessController;
import chess.dto.RequestDto;
import chess.dto.ResponseDto;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @PostMapping("/room")
    public String createRoom(@ModelAttribute RequestDto requestDto) {
        final int roomId = controller.startGame(requestDto);
        return "redirect:/room/" + roomId;
    }

    @GetMapping("/room/{roomId}")
    public String showRoom(@PathVariable("roomId") int id, Model model) {
        model.addAttribute("roomId", id);
        model.addAttribute("board", controller.getBoard(id));
        return "index";
    }

    @ResponseBody
    @PostMapping("/room/{roomId}/move")
    public String movePiece(@PathVariable("roomId") int id, @RequestBody String body) {
        final String[] split = body.split("=");
        ResponseDto response = controller.move(id, split[1]);
        return response.toString();
    }
}
