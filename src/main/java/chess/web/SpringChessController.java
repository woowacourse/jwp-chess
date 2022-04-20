package chess.web;

import chess.web.dao.PlayerDao;
import chess.web.dto.PlayerDto;
import chess.web.service.PlayerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SpringChessController {

    private PlayerService playerService = new PlayerService(new PlayerDao());

    @GetMapping("/")
    public String login() {
        return "login.html";
    }


    @PostMapping(value = "/board")
    public String createPlayer(@RequestParam String name) {
        PlayerDto player = playerService.login(name);
        return "redirect:/board?playerId=" + player.getId();
    }

    @GetMapping("/board")
    public String board() {
        return "/board.html";
    }

}
