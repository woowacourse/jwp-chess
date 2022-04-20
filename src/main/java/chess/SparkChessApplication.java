package chess;

import chess.controller.WebChessController;

public class SparkChessApplication {
    public static void main(String[] args) {
        new WebChessController().run();
    }
    //     get("/", (req, res) -> {
    //         Map<String, Object> model = new HashMap<>();
    //         return render(model, "index.hbs");
    //     });
    // }
    //
    // private static String render(Map<String, Object> model, String templatePath) {
    //     return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
    // }
}
