package chess.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import chess.service.RoomService;
import chess.web.dto.RoomDto;

@Controller
public class HomeController {

    @GetMapping("/")
    public String login() {
        return "login.html";
    }
}
