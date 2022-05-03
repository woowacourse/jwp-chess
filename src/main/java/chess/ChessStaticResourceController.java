package chess;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChessStaticResourceController {

    @GetMapping("/")
    public String index() {
        return "room.html";
    }
}
