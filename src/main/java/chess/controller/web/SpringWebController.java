package chess.controller.web;

import java.util.HashMap;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpringWebController {

    @GetMapping("/")
    public String index(Model model) {
        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("test1", "SUCCESS");
        attributes.put("test2", "YES!!!!");
        model.addAllAttributes(attributes);
        return "index";
    }
}
