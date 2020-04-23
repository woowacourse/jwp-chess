package spring.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.service.ChessService;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

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

    @GetMapping("/main")
    String main() {
        Map<String, Object> model = new HashMap<>();
        return render(model, "start.html");
    }

    private static String render(Map<String, Object> model, String templatePath) {
        return handlebarsTemplateEngine.render(new ModelAndView(model, templatePath));
    }
}
