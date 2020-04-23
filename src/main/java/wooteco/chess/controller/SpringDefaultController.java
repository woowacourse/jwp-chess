package wooteco.chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SpringDefaultController {

    @RequestMapping("/")
    public String index() {
        return "redirect:/rooms";
    }


}
