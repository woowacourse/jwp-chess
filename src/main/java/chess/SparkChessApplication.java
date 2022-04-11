package chess;

import static spark.Spark.staticFileLocation;

import chess.controller.WebController;


public class SparkChessApplication {
//    public static void main(String[] args) {
//        get("/", (req, res) -> {
//            Map<String, Object> model = new HashMap<>();
//            return render(model, "index.hbs");
//        });
//    }

    public static void main(String[] args) {
        staticFileLocation("/static");
        WebController webController = new WebController();
        webController.run();
    }
}
