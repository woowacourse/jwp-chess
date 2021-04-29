package chess.web;

import chess.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final RoomService service;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("rooms", service.findAllDesc());
        return "index";
    }
}
