package chess;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChessSpringController {

    @GetMapping("/")
    public String index() {
        return "roby";
    }
}
