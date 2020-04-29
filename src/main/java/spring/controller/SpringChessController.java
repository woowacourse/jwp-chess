package spring.controller;

import com.google.gson.Gson;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;
import spring.service.ChessService;

import java.util.HashMap;
import java.util.Map;

// TODO : Controller 분리 기준 알아보자.
@RestController
public class SpringChessController {
    private static final HandlebarsTemplateEngine handlebarsTemplateEngine = new HandlebarsTemplateEngine();

    private final ChessService chessService;

    // TODO : 생성자 주입이 더 나은 이유
    public SpringChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String main() {
        Map<String, Object> model = new HashMap<>();
        return render(model, "start.html");
    }

    @GetMapping("/start")
    public String start() {
        Map<String, Object> model = new HashMap<>();
        // 받았다.
        // game_id, 혹은 select할 수있는 뭔가;
        return render(model, "index.html");
    }

    @PostMapping("/api/game")
    public String starts() {
        return new Gson().toJson(chessService.makeChessBoard());
    }

    private static String render(Map<String, Object> model, String templatePath) {
        return handlebarsTemplateEngine.render(new ModelAndView(model, templatePath));
    }
}
